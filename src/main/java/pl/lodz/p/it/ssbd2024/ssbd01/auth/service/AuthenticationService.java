package pl.lodz.p.it.ssbd2024.ssbd01.auth.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthHistoryRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.JWTWhitelistRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.mail.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.RunAs;
import pl.lodz.p.it.ssbd2024.ssbd01.util.TokenGenerator;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2024.ssbd01.util.Utils.calculateExpirationDate;

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
    private final AccountMokHistoryRepository accountMokHistoryRepository;
    private final AccountAuthHistoryRepository accountAuthHistoryRepository;
    private final MailService mailService;
    private final ConfigurationProperties config;


    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void registerAccount(Account account) {
        var savedAccount = accountMokRepository.saveAndFlush(account);
        var randString = TokenGenerator.generateToken();

        accountMokHistoryRepository.saveAndFlush(new AccountHistory(savedAccount));

        var expirationHours = config.getConfirmationTokenExpiration();
        var expirationDate = calculateExpirationDate(expirationHours);
        var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);
        var confirmationReminder = new ConfirmationReminder(savedAccount, savedAccount.getCreatedAt()
                .plusHours(expirationHours / 2).plusMinutes(expirationHours % 2 * 30));

        accountConfirmationRepository.saveAndFlush(newAccountConfirmation);
        confirmationReminderRepository.saveAndFlush(confirmationReminder);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(savedAccount));
        RunAs.runAsSystem(() -> mailService.sendEmailToVerifyAccount(savedAccount, randString));
    }

    @PreAuthorize("permitAll()")
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            noRollbackFor = {BadCredentialsException.class},
            timeoutString = "${transaction.timeout}"
    )
    @Retryable(
            retryFor = {OptimisticLockException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public String authenticate(LoginDTO loginDTO, String language) throws AccountLockedException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        } catch (BadCredentialsException e) {
            Account account = accountAuthRepository.findByUsername(loginDTO.username());
            account.setFailedLoginAttempts(account.getFailedLoginAttempts() + 1);
            account.setLastFailedLogin(LocalDateTime.now());

            HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            account.setLastFailedLoginIp(
                    curRequest.getHeader("X-Forwarded-For") != null ? curRequest.getHeader("X-Forwarded-For") : curRequest.getRemoteAddr());

            if (account.getFailedLoginAttempts() >= config.getAuthAttempts()) {
                account.setNonLocked(false);

                LocalDateTime lockTimeout = LocalDateTime.now().plusSeconds(config.getAuthLockTime());
                account.setLockedUntil(lockTimeout);
                jwtWhitelistRepository.deleteAllByAccount_Id(account.getId());
                RunAs.runAsSystem(() -> mailService.sendEmailAfterFailedLoginAttempts(account, lockTimeout));
            }

            accountAuthRepository.saveAndFlush(account);
            accountAuthHistoryRepository.saveAndFlush(new AccountHistory(account));
            throw e;
        }

        var account = accountAuthRepository.findByUsername(loginDTO.username());
        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var ipAddress =
                curRequest.getHeader("X-Forwarded-For") != null
                        ? curRequest.getHeader("X-Forwarded-For")
                        : curRequest.getRemoteAddr();
        String[] primaryLang = {language};
        if (language.contains(",")) {
            primaryLang = language.split(",");
        }
        if (primaryLang[0].contains("pl")) {
            account.setLanguage(LanguageEnum.POLISH);
        } else if (primaryLang[0].contains("en")) {
            account.setLanguage(LanguageEnum.ENGLISH);
        }

        if (account.getRoles().contains(new Role(AccountRoleEnum.ROLE_ADMIN))) {
            RunAs.runAsSystem(() -> mailService.sendEmailAdminNewLogin(account, ipAddress));
        }

        account.setFailedLoginAttempts(0);
        account.setLastSuccessfulLogin(LocalDateTime.now());
        account.setLastSuccessfulLoginIp(ipAddress);

        accountAuthRepository.saveAndFlush(account);
        accountAuthHistoryRepository.saveAndFlush(new AccountHistory(account));

        return jwtService.generateToken(new HashMap<>(), account);
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @Retryable(
            retryFor = {OptimisticLockException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public void verifyAccount(String token)
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
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(account));
        accountConfirmationRepository.delete(accountConfirmation);
        confirmationReminderRepository.deleteByAccount(account);

        RunAs.runAsSystem(() -> mailService.sendEmailToInformAboutVerification(account));
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @Retryable(
            retryFor = {OptimisticLockException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public void unlockAccountThatWasNotUsed(String token) throws AccountUnlockTokenNotFoundException {
        AccountUnlock accountUnlock = accountUnlockRepository.findByToken(token)
                .orElseThrow(() -> new AccountUnlockTokenNotFoundException(ExceptionMessages.UNLOCK_TOKEN_NOT_FOUND));

        Account account = accountUnlock.getAccount();
        account.setNonLocked(true);
        accountUnlockRepository.delete(accountUnlock);

        var returnAccount = accountMokRepository.saveAndFlush(account);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnAccount));

        RunAs.runAsSystem(() -> mailService.sendEmailToInformAboutUnblockAccount(returnAccount));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
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

                mailService.sendEmailOnDeleteUnverifiedAccount(account);
                passwordHistoryRepository.deletePasswordHistoriesByAccount(account);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void unlockAccounts() {
        LocalDateTime now = LocalDateTime.now();

        List<Account> lockedAccounts = accountMokRepository.findByNonLockedFalseAndLockedUntilBefore(now);

        for (Account account : lockedAccounts) {
            account.setNonLocked(true);
            account.setFailedLoginAttempts(0);
            account.setLockedUntil(null);
            accountMokRepository.saveAndFlush(account);
            accountMokHistoryRepository.saveAndFlush(new AccountHistory(account));

            mailService.sendEmailToInformAboutUnblockAccount(account);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void lockAccountsThatAreNotUsed() {
        LocalDateTime now = LocalDateTime.now();

        List<Account> accounts = accountMokRepository.findByNonLockedTrueAndLastSuccessfulLoginBefore(
                now.minusDays(config.getAuthLockTimeUnusedAccountDays()));

        for (Account account : accounts) {
            account.setNonLocked(false);
            accountMokRepository.saveAndFlush(account);
            accountMokHistoryRepository.saveAndFlush(new AccountHistory(account));

            var token = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
            var accountUnlock = new AccountUnlock(token, account);
            accountUnlockRepository.saveAndFlush(accountUnlock);

            mailService.sendEmailToUnblockAccountViaLink(account, token);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredJWTTokensFromWhitelist() {
        jwtWhitelistRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void sendAccountConfirmationReminder() {
        confirmationReminderRepository.findByReminderDateBefore(LocalDateTime.now()).forEach(confirmationReminder -> {
            AccountConfirmation confirmation =
                    accountConfirmationRepository.findByAccount_Id(confirmationReminder.getAccount().getId()).orElseThrow();

            mailService.sendEmailConfirmationReminder(confirmation.getAccount(), confirmation.getToken());

            confirmationReminderRepository.deleteById(confirmationReminder.getId());
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public void logout(String token) {
        jwtWhitelistRepository.deleteByToken(token);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public String refreshJWT(String token) throws TokenNotFoundException, AccountLockedException {
        JWTWhitelistToken jwtWhitelistToken = jwtWhitelistRepository.findByToken(token.substring(7)).orElseThrow(
                () -> new TokenNotFoundException(ExceptionMessages.TOKEN_NOT_FOUND));

        Account account = jwtWhitelistToken.getAccount();
        jwtWhitelistRepository.delete(jwtWhitelistToken);
        jwtWhitelistRepository.flush();

        return jwtService.generateToken(new HashMap<>(), account);
    }
}