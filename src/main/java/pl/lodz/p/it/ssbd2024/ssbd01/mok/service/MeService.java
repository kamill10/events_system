package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.mail.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.RunAs;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeService {

    private final AccountMokRepository accountMokRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordHistoryRepository passwordHistoryRepository;

    private final AccountMokHistoryRepository accountMokHistoryRepository;

    private final ChangeMyPasswordRepository changeMyPasswordRepository;

    private final ChangeEmailRepository changeEmailRepository;

    private final ConfigurationProperties config;

    private final ServiceVerifier verifier;

    private final ThemeRepository themeRepository;

    private final TimeZoneRepository timeZoneRepository;

    private final MailService mailService;

    private final CredentialResetRepository resetCredentialRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account getAccount() throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        return accountMokRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void changeMyPasswordSendMail(String currentPassword, String newPassword)
            throws WrongOldPasswordException, ThisPasswordAlreadyWasSetInHistory {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new WrongOldPasswordException(ExceptionMessages.WRONG_OLD_PASSWORD);
        }

        if (verifier.isPasswordInHistory(account.getId(), newPassword)) {
            throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
        }

        changeMyPasswordRepository.deleteByAccount(account);
        changeMyPasswordRepository.flush();
        resetCredentialRepository.deleteByAccount(account);
        resetCredentialRepository.flush();

        var expiration = config.getCredentialChangeTokenExpiration();
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyPassword(account, expirationDate, passwordEncoder.encode(newPassword));
        changeMyPasswordRepository.saveAndFlush(newResetIssue);

        RunAs.runAsSystem(() -> mailService.sendEmailToChangeMyPassword(newResetIssue));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void changeMyEmailSendMail(String currentPassword, String newEmail)
            throws WrongOldPasswordException, EmailAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new WrongOldPasswordException(ExceptionMessages.WRONG_OLD_PASSWORD);
        }

        if (accountMokRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_EXISTS);
        }

        changeEmailRepository.deleteByAccount(account);
        changeEmailRepository.flush();

        var expiration = config.getCredentialChangeTokenExpiration();
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeEmail(account, expirationDate, newEmail);
        changeEmailRepository.saveAndFlush(newResetIssue);

        RunAs.runAsSystem(() -> mailService.sendEmailToChangeMyEmail(newResetIssue, newEmail));
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void changeMyPasswordWithToken(String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException, AccountLockedException, AccountNotVerifiedException {
        Account account = verifier.verifyCredentialReset(token, changeMyPasswordRepository);

        String password =
                changeMyPasswordRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.TOKEN_NOT_FOUND))
                        .getPassword();
        account.setPassword(password);

        passwordHistoryRepository.saveAndFlush(new PasswordHistory(account));
        accountMokRepository.saveAndFlush(account);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(account));
        changeMyPasswordRepository.deleteByToken(token);
    }


    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void changeMyEmailWithToken(String token)
            throws AccountNotFoundException, TokenNotFoundException, TokenExpiredException, AccountLockedException, AccountNotVerifiedException {
        Account accountToUpdate = verifier.verifyCredentialReset(token, changeEmailRepository);
        var changeMyEmail = changeEmailRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.EMAIL_RESET_TOKEN_NOT_FOUND));
        accountToUpdate.setEmail(changeMyEmail.getEmail());

        accountMokRepository.saveAndFlush(accountToUpdate);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(accountToUpdate));
        changeEmailRepository.deleteByToken(token);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account updateMyAccountData(Account accountData, String eTag, String theme, String timeZone)
            throws AccountNotFoundException, OptLockException, TimeZoneNotFoundException, AccountThemeNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Account accountToUpdate = accountMokRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        if (!ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

        if (!timeZone.isBlank()) {
            AccountTimeZone accountTimeZone = timeZoneRepository.findByTimeZone(timeZone)
                    .orElseThrow(() -> new TimeZoneNotFoundException(ExceptionMessages.TIME_ZONE_NOT_FOUND));
            accountToUpdate.setAccountTimeZone(accountTimeZone);
        }

        if (!theme.isBlank()) {
            AccountTheme accountTheme = themeRepository.findByTheme(theme)
                    .orElseThrow(() -> new AccountThemeNotFoundException(ExceptionMessages.ACCOUNT_THEME_NOT_FOUND));
            accountToUpdate.setAccountTheme(accountTheme);
        }

        accountToUpdate.setFirstName(accountData.getFirstName());
        accountToUpdate.setLastName(accountData.getLastName());
        accountToUpdate.setGender(accountData.getGender());
        var returnedAccount = accountMokRepository.saveAndFlush(accountToUpdate);

        accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnedAccount));
        return returnedAccount;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void logSwitchRole(AccountRoleEnum roleEnum) throws AccountNotFoundException, RoleNotAssignedToAccount {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Account accountToLog = accountMokRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        List<Role> accountRoles = accountToLog.getRoles();

        if (!accountRoles.contains(new Role(roleEnum))) {
            throw new RoleNotAssignedToAccount(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void setAccountTheme(String theme) throws AccountThemeNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        AccountTheme accountTheme = themeRepository.findByTheme(theme)
                .orElseThrow(() -> new AccountThemeNotFoundException(ExceptionMessages.ACCOUNT_THEME_NOT_FOUND));
        account.setAccountTheme(accountTheme);

        accountMokRepository.saveAndFlush(account);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(account));
    }

}
