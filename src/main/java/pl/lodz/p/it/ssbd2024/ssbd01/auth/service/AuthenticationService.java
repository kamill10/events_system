package pl.lodz.p.it.ssbd2024.ssbd01.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.JWTWhitelistRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountConfirmation;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ConfirmationReminder;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.PasswordHistory;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.RoleNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:expiration.properties")
public class AuthenticationService {
    private final AccountMokRepository accountMokRepository;
    private final AccountAuthRepository accountAuthRepository;
    private final AccountConfirmationRepository accountConfirmationRepository;
    private final ConfirmationReminderRepository confirmationReminderRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final JWTWhitelistRepository jwtWhitelistRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final MailService mailService;


    @Value("${auth.attempts:3}")
    private int maxFailedLoginAttempts;

    @Value("${auth.lock-time:86400}")
    private int lockTimeout;

    private LocalDateTime calculateExpirationDate(int expirationHours) {
        return LocalDateTime.now().plusHours(expirationHours);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void registerUser(Account account) {
        var savedAccount = accountMokRepository.saveAndFlush(account);
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expirationHours = Integer.parseInt(Objects.requireNonNull(env.getProperty("confirmation.token.expiration.hours")));
        var expirationDate = calculateExpirationDate(expirationHours);
        var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/verify-account?token=");
        sb.append(randString);
        sb.append("'>Link</a>");
        mailService.sendEmail(account, "mail.verify.account.subject",
                "mail.verify.account.body", new Object[] {sb});
        var confirmationReminder = new ConfirmationReminder(savedAccount, savedAccount.getCreatedAt()
                .plusHours(expirationHours / 2).plusMinutes(expirationHours % 2 * 30));
        accountConfirmationRepository.saveAndFlush(newAccountConfirmation);
        confirmationReminderRepository.saveAndFlush(confirmationReminder);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(savedAccount));

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, noRollbackFor = {BadCredentialsException.class})
    public String authenticate(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.username(),
                            loginDTO.password()
                    )
            );
        } catch (BadCredentialsException e) {
            updateFailedLoginAttempts(loginDTO.username());
            throw e;
        }

        var user = accountAuthRepository.findByUsername(loginDTO.username());
        user.setFailedLoginAttempts(0);
        user.setLastSuccessfulLogin(LocalDateTime.now());
        accountAuthRepository.save(user);
        return jwtService.generateToken(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void verifyAccount(String token)
            throws AccountConfirmationTokenNotFoundException, AccountConfirmationTokenExpiredException, AccountNotFoundException,
            RoleNotFoundException {
        var accountConfirmation = accountConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new AccountConfirmationTokenNotFoundException(ExceptionMessages.CONFIRMATION_TOKEN_NOT_FOUND));
        if (accountConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AccountConfirmationTokenExpiredException(ExceptionMessages.CONFIRMATION_TOKEN_EXPIRED);
        }
        var accountId = accountConfirmation.getAccount().getId();
        var account = accountMokRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setVerified(true);
        account.addRole(roleRepository.findByName(AccountRoleEnum.PARTICIPANT)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND)));
        accountMokRepository.saveAndFlush(account);
        accountConfirmationRepository.delete(accountConfirmation);
        confirmationReminderRepository.deleteByAccount(account);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Scheduled(fixedRate = 120000)
    public void deleteExpiredTokensAndAccounts() {
        LocalDateTime now = LocalDateTime.now();

        List<AccountConfirmation> expiredTokens = accountConfirmationRepository.findByExpirationDateBefore(now);

        for (AccountConfirmation token : expiredTokens) {
            Optional<Account> optionalAccount = accountMokRepository.findById(token.getAccount().getId());
            accountConfirmationRepository.delete(token);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                accountMokRepository.delete(account);
                mailService.sendEmail(account, "mail.delete.account.subject", "mail.delete.account.body", null);
                passwordHistoryRepository.deletePasswordHistoriesByAccount(account);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Scheduled(fixedRate = 120000)
    public void unlockAccounts() {
        LocalDateTime now = LocalDateTime.now();

        List<Account> lockedAccounts = accountMokRepository.findByNonLockedFalseAndLockedUntilBefore(now);

        for (Account account : lockedAccounts) {
            account.setNonLocked(true);
            account.setFailedLoginAttempts(0);
            account.setLockedUntil(null);
            accountMokRepository.save(account);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Scheduled(fixedRate = 120000)
    public void deleteExpiredJWTTokensFromWhitelist() {
        jwtWhitelistRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Scheduled(fixedRate = 120000)
    public void sendAccountConfirmationReminder() {
        confirmationReminderRepository.findByReminderDateBefore(LocalDateTime.now()).forEach(confirmationReminder -> {
            AccountConfirmation confirmation =
                    accountConfirmationRepository.findByAccount_Id(confirmationReminder.getAccount().getId()).orElseThrow();
            StringBuilder sb = new StringBuilder();
            sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/verify-account?token=");
            sb.append(confirmation.getToken());
            sb.append("'>Link</a>");
            mailService.sendEmail(confirmation.getAccount(), "mail.verify.account.subject",
                    "mail.verify.account.body", new Object[] {sb});
            confirmationReminderRepository.deleteById(confirmationReminder.getId());
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void logout(String token) {
        jwtWhitelistRepository.deleteByToken(token);
    }

    public void updateFailedLoginAttempts(String username) {
        Account account = accountAuthRepository.findByUsername(username);
        account.setFailedLoginAttempts(account.getFailedLoginAttempts() + 1);
        account.setLastFailedLogin(LocalDateTime.now());
        HttpServletRequest curRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        account.setLastFailedLoginIp(
                curRequest.getHeader("X-Forwarded-For") != null ? curRequest.getHeader("X-Forwarded-For") : curRequest.getRemoteAddr());
        if (account.getFailedLoginAttempts() >= maxFailedLoginAttempts) {
            account.setNonLocked(false);
            LocalDateTime lockTimeout = LocalDateTime.now().plusSeconds(this.lockTimeout);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            account.setLockedUntil(lockTimeout);
            jwtWhitelistRepository.deleteAllByAccount_Id(account.getId());
            mailService.sendEmail(account, "mail.locked.until.subject", "mail.locked.until.body", new Object[] {lockTimeout.format(formatter)});
        }
        accountAuthRepository.save(account);
    }

}