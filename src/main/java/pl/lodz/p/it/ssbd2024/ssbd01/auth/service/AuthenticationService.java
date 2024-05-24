package pl.lodz.p.it.ssbd2024.ssbd01.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
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
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.MailToVerifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountUnlockTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.RoleNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.TokenGenerator;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountMokRepository accountMokRepository;
    private final AccountAuthRepository accountAuthRepository;
    private final AccountConfirmationRepository accountConfirmationRepository;
    private final ConfirmationReminderRepository confirmationReminderRepository;
    private final AccountUnlockRepository accountUnlockRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final JWTWhitelistRepository jwtWhitelistRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final MailService mailService;
    private final ConfigurationProperties config;

    private LocalDateTime calculateExpirationDate(int expirationHours) {
        return LocalDateTime.now().plusHours(expirationHours);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public MailToVerifyDTO registerUser(Account account) {
        var savedAccount = accountMokRepository.saveAndFlush(account);
        var randString = TokenGenerator.generateToken();
        var expirationHours = config.getConfirmationTokenExpiration();
        var expirationDate = calculateExpirationDate(expirationHours);
        var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);
        var confirmationReminder = new ConfirmationReminder(savedAccount, savedAccount.getCreatedAt()
                .plusHours(expirationHours / 2).plusMinutes(expirationHours % 2 * 30));
        accountConfirmationRepository.saveAndFlush(newAccountConfirmation);
        confirmationReminderRepository.saveAndFlush(confirmationReminder);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(savedAccount));
        return new MailToVerifyDTO(savedAccount, randString);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, noRollbackFor = {BadCredentialsException.class})
    public String authenticate(LoginDTO loginDTO, String language) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        } catch (BadCredentialsException e) {
            Account account = accountAuthRepository.findByUsername(loginDTO.username());
            account.setFailedLoginAttempts(account.getFailedLoginAttempts() + 1);
            account.setLastFailedLogin(LocalDateTime.now());
            HttpServletRequest curRequest =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest();
            account.setLastFailedLoginIp(
                    curRequest.getHeader("X-Forwarded-For") != null ? curRequest.getHeader("X-Forwarded-For") : curRequest.getRemoteAddr());
            if (account.getFailedLoginAttempts() >= config.getAuthAttempts()) {
                account.setNonLocked(false);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime lockTimeout = LocalDateTime.now().plusSeconds(config.getAuthLockTime());
                account.setLockedUntil(lockTimeout);
                jwtWhitelistRepository.deleteAllByAccount_Id(account.getId());
                mailService.sendEmailTemplate(account, "mail.locked.until.subject", "mail.locked.until.body",
                        new Object[]{lockTimeout.format(formatter)});
            }
            accountAuthRepository.saveAndFlush(account);
            throw e;
        }

        var user = accountAuthRepository.findByUsername(loginDTO.username());
        String[] primaryLang = {language};
        if (language.contains(",")) {
            primaryLang = language.split(",");
        }
        if (primaryLang[0].contains("pl")) {
            user.setLanguage(LanguageEnum.POLISH);
        } else if (primaryLang[0].contains("en")) {
            user.setLanguage(LanguageEnum.ENGLISH);
        }

        user.setFailedLoginAttempts(0);
        user.setLastSuccessfulLogin(LocalDateTime.now());
        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        user.setLastSuccessfulLoginIp(
                curRequest.getHeader("X-Forwarded-For") != null ? curRequest.getHeader("X-Forwarded-For") : curRequest.getRemoteAddr());
        accountAuthRepository.saveAndFlush(user);
        return jwtService.generateToken(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account verifyAccount(String token)
            throws AccountConfirmationTokenNotFoundException, AccountConfirmationTokenExpiredException, AccountNotFoundException,
            RoleNotFoundException {
        var accountConfirmation = accountConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new AccountConfirmationTokenNotFoundException(ExceptionMessages.CONFIRMATION_TOKEN_NOT_FOUND));
        if (accountConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AccountConfirmationTokenExpiredException(ExceptionMessages.CONFIRMATION_TOKEN_EXPIRED);
        }
        var accountId = accountConfirmation.getAccount().getId();
        var account = accountMokRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setVerified(true);
        account.addRole(roleRepository.findByName(AccountRoleEnum.ROLE_PARTICIPANT)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND)));
        accountMokRepository.saveAndFlush(account);
        accountConfirmationRepository.delete(accountConfirmation);
        confirmationReminderRepository.deleteByAccount(account);
        return account;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account unlockAccountThatWasNotUsed(String token) throws AccountUnlockTokenNotFoundException {
        AccountUnlock accountUnlock = accountUnlockRepository.findByToken(token)
                .orElseThrow(() -> new AccountUnlockTokenNotFoundException(ExceptionMessages.UNLOCK_TOKEN_NOT_FOUND));
        Account account = accountUnlock.getAccount();
        account.setNonLocked(true);
        accountUnlockRepository.delete(accountUnlock);
        return accountMokRepository.saveAndFlush(account);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredTokensAndAccounts() {
        LocalDateTime now = LocalDateTime.now();

        List<AccountConfirmation> expiredTokens = accountConfirmationRepository.findByExpirationDateBefore(now);

        for (AccountConfirmation token : expiredTokens) {
            Optional<Account> optionalAccount = accountMokRepository.findById(token.getAccount().getId());
            accountConfirmationRepository.delete(token);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                accountMokRepository.delete(account);
                mailService.sendEmailTemplate(account, "mail.delete.account.subject", "mail.delete.account.body", null);
                passwordHistoryRepository.deletePasswordHistoriesByAccount(account);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void unlockAccounts() {
        LocalDateTime now = LocalDateTime.now();

        List<Account> lockedAccounts = accountMokRepository.findByNonLockedFalseAndLockedUntilBefore(now);

        for (Account account : lockedAccounts) {
            account.setNonLocked(true);
            account.setFailedLoginAttempts(0);
            account.setLockedUntil(null);
            accountMokRepository.saveAndFlush(account);
            mailService.sendEmailToInformAboutUnblockAccount(account);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void lockAccountsThatAreNotUsed() {
        LocalDateTime now = LocalDateTime.now();

        List<Account> accounts = accountMokRepository.findByNonLockedTrueAndLastSuccessfulLoginBefore(
                now.minusDays(config.getAuthLockTimeUnusedAccountDays()));

        for (Account account : accounts) {
            account.setNonLocked(false);
            accountMokRepository.saveAndFlush(account);
            var token = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
            var accountUnlock = new AccountUnlock(token, account);
            accountUnlockRepository.saveAndFlush(accountUnlock);
            mailService.sendEmailToUnblockAccountViaLink(account, token);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredJWTTokensFromWhitelist() {
        jwtWhitelistRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void sendAccountConfirmationReminder() {
        confirmationReminderRepository.findByReminderDateBefore(LocalDateTime.now()).forEach(confirmationReminder -> {
            AccountConfirmation confirmation =
                    accountConfirmationRepository.findByAccount_Id(confirmationReminder.getAccount().getId()).orElseThrow();
            StringBuilder sb = new StringBuilder();
            sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/verify-account?token=");
            sb.append(confirmation.getToken());
            sb.append("'>Link</a>");
            mailService.sendEmailTemplate(confirmation.getAccount(), "mail.verify.account.subject",
                    "mail.verify.account.body", new Object[]{sb});
            confirmationReminderRepository.deleteById(confirmationReminder.getId());
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public void logout(String token) {
        jwtWhitelistRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountByUsername(String username) {
        return accountAuthRepository.findByUsername(username);
    }

}