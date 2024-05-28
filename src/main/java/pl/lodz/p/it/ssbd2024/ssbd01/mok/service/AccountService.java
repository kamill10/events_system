package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountPageDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.RunAs;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ServiceVerifier;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.lodz.p.it.ssbd2024.ssbd01.util.Utils.canAddManagerOrAdminRole;
import static pl.lodz.p.it.ssbd2024.ssbd01.util.Utils.canAddParticipantRole;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMokRepository accountMokRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ChangeMyPasswordRepository changeMyPasswordRepository;
    private final ChangeEmailRepository changeEmailRepository;
    private final CredentialResetRepository resetCredentialRepository;
    private final AccountMokHistoryRepository accountMokHistoryRepository;
    private final ConfigurationProperties config;
    private final ServiceVerifier verifier;
    private final MailService mailService;
    private final TimeZoneRepository timeZoneRepository;
    private final ThemeRepository themeRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Page<Account> getAccountsPage(GetAccountPageDTO getAccountPageDTO) {
        Sort sort = getAccountPageDTO.buildSort();
        Pageable pageable = PageRequest.of(getAccountPageDTO.page(), getAccountPageDTO.elementPerPage(), sort);

        if (getAccountPageDTO.phrase() != null && !getAccountPageDTO.phrase().isEmpty()) {
            String phrase = "%" + getAccountPageDTO.phrase().toLowerCase() + "%";
            return accountMokRepository.findAllByPhrase(phrase, pageable);
        }

        return accountMokRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account addAccount(Account account) {
        Account returnedAccount = accountMokRepository.saveAndFlush(account);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnedAccount));
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(returnedAccount));

        return returnedAccount;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account addRoleToAccount(UUID id, AccountRoleEnum roleName, String eTag)
            throws RoleAlreadyAssignedException, WrongRoleToAccountException, RoleNotFoundException,
            AccountNotFoundException, OptLockException {
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();

        if (accountRoles.contains(new Role(roleName))) {
            throw new RoleAlreadyAssignedException(ExceptionMessages.ROLE_ALREADY_ASSIGNED);
        }

        if (!ETagBuilder.isETagValid(eTag, String.valueOf(account.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

        switch (roleName) {
            case ROLE_PARTICIPANT:
                canAddParticipantRole(account);
                break;
            case ROLE_MANAGER, ROLE_ADMIN:
                canAddManagerOrAdminRole(account);
                if (!account.getVerified()) {
                    account.setVerified(true);
                }
                break;
            default:
                throw new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND);
        }

        account.addRole(role);
        var returnedAccount = accountMokRepository.saveAndFlush(account);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnedAccount));

        RunAs.runAsSystem(() -> mailService.sendEmailToAddRoleToAccount(returnedAccount, roleName.name()));

        return returnedAccount;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account removeRoleFromAccount(UUID id, AccountRoleEnum roleName, String eTag)
            throws RoleNotFoundException, AccountNotFoundException, RoleCanNotBeRemoved, OptLockException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        if (!ETagBuilder.isETagValid(eTag, String.valueOf(account.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

        for (Role roles : account.getRoles()) {
            if (roles.getName().equals(roleName)) {
                account.removeRole(role);
                var returnedAccount = accountMokRepository.saveAndFlush(account);
                accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnedAccount));
                RunAs.runAsSystem(() -> mailService.sendEmailToRemoveRoleFromAccount(account, roleName.name()));
                return returnedAccount;
            }
        }

        throw new RoleCanNotBeRemoved(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account setAccountStatus(UUID id, boolean status, String eTag) throws AccountNotFoundException, OptLockException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        if (!ETagBuilder.isETagValid(eTag, String.valueOf(account.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

        account.setActive(status);
        var returnedAccount = accountMokRepository.saveAndFlush(account);
        accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnedAccount));

        if (status) {
            RunAs.runAsSystem(() -> mailService.sendEmailToSetActiveAccount(returnedAccount));
        } else {
            RunAs.runAsSystem(() -> mailService.sendEmailToSetInactiveAccount(returnedAccount));
        }

        return returnedAccount;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account updateAccountData(UUID id, Account account, String eTag) throws AccountNotFoundException, OptLockException {
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        if (!ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setGender(account.getGender());
        var returnedAccount = accountMokRepository.saveAndFlush(accountToUpdate);

        accountMokHistoryRepository.saveAndFlush(new AccountHistory(returnedAccount));

        return returnedAccount;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public List<Account> getParticipants() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.ROLE_PARTICIPANT)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

        return accountMokRepository.findAccountByRolesContains(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public List<Account> getManagers() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.ROLE_MANAGER)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

        return accountMokRepository.findAccountByRolesContains(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public List<Account> getAdmins() throws RoleNotFoundException {
        Role role =
                roleRepository.findByName(AccountRoleEnum.ROLE_ADMIN).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

        return accountMokRepository.findAccountByRolesContains(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public Account getAccountById(UUID id) throws AccountNotFoundException {
        return accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void resetPasswordAndSendEmail(String email) {
        Optional<Account> account = accountMokRepository.findByEmail(email);

        if (account.isEmpty()) {
            return;
        }

        resetCredentialRepository.deleteByAccount(account.get());
        resetCredentialRepository.flush();

        CredentialReset credentialReset = verifier.saveTokenToResetCredential(account.get());

        RunAs.runAsSystem(() -> mailService.sendEmailToResetPassword(credentialReset));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void changePasswordByAdminAndSendEmail(String email) throws AccountNotFoundException {
        Account account = accountMokRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        resetCredentialRepository.deleteByAccount(account);
        resetCredentialRepository.flush();

        account.setNonLocked(false);
        accountMokRepository.saveAndFlush(account);

        CredentialReset credentialReset = verifier.saveTokenToResetCredential(account);

        RunAs.runAsSystem(() -> mailService.sendEmailToChangePasswordByAdmin(credentialReset));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void sendMailWhenEmailChangeByAdmin(UUID id, String email) throws AccountNotFoundException, EmailAlreadyExistsException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        if (accountMokRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_EXISTS);
        }

        changeEmailRepository.deleteByAccount(account);
        changeEmailRepository.flush();

        var expiration = config.getCredentialChangeTokenExpiration();
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeEmail(account, expirationDate, email);
        changeEmailRepository.saveAndFlush(newResetIssue);

        RunAs.runAsSystem(() -> mailService.sendEmailToChangeEmailByAdmin(newResetIssue, email));
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void resetPasswordWithToken(String token, String newPassword)
            throws AccountNotFoundException, TokenExpiredException, ThisPasswordAlreadyWasSetInHistory, TokenNotFoundException,
            AccountLockedException, AccountNotVerifiedException {
        Account accountToUpdate = verifier.verifyCredentialReset(token, resetCredentialRepository);

        if (verifier.isPasswordInHistory(accountToUpdate.getId(), newPassword)) {
            throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
        }

        accountToUpdate.setPassword(passwordEncoder.encode(newPassword));
        accountToUpdate.setNonLocked(true);

        passwordHistoryRepository.saveAndFlush(new PasswordHistory(accountToUpdate));
        accountMokRepository.saveAndFlush(accountToUpdate);
        resetCredentialRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AccountHistory> getAllAccountHistoryByUsername(String username) {
        return accountMokHistoryRepository.findAllByAccount_Username(username);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void addTimeZone(String timeZone) throws TimeZoneNotFoundException {
        try {
            ZoneId zoneId = ZoneId.of(timeZone);
            AccountTimeZone timeZone1 = new AccountTimeZone(timeZone);
            timeZoneRepository.saveAndFlush(timeZone1);
        } catch (DateTimeException e) {
            throw new TimeZoneNotFoundException(ExceptionMessages.TIME_ZONE_NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void addTheme(String theme) {
        AccountTheme accountTheme = new AccountTheme(theme);
        themeRepository.saveAndFlush(accountTheme);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredResetCredentialTokens() {
        resetCredentialRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredChangePasswordTokens() {
        changeMyPasswordRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredChangeEmailTokens() {
        changeEmailRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

}


