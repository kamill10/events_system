package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ServiceVerifier;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;


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
    private final ConfigurationProperties config;
    private final ServiceVerifier verifier;
    private final ResetPasswordUnauthorizedTokenRepository resetPasswordUnauthorizedTokenRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addAccount(Account account) {
        Account returnedAccount = accountMokRepository.saveAndFlush(account);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(returnedAccount));
        return returnedAccount;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addRoleToAccount(UUID id, AccountRoleEnum roleName)
            throws RoleAlreadyAssignedException, WrongRoleToAccountException, RoleNotFoundException,
            AccountNotFoundException {
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(roleName))) {
            throw new RoleAlreadyAssignedException(ExceptionMessages.ROLE_ALREADY_ASSIGNED);
        }
        switch (roleName) {
            case ROLE_PARTICIPANT:
                canAddParticipantRole(account);
                break;
            case ROLE_MANAGER, ROLE_ADMIN:
                canAddManagerOrAdminRole(account);
                break;
            default:
                throw new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND);
        }
        account.addRole(role);
        return accountMokRepository.saveAndFlush(account);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private void canAddManagerOrAdminRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(AccountRoleEnum.ROLE_PARTICIPANT))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private void canAddParticipantRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.isEmpty()) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account removeRoleFromAccount(UUID id, AccountRoleEnum roleName)
            throws RoleNotFoundException, AccountNotFoundException, RoleCanNotBeRemoved {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        for (Role roles : account.getRoles()) {
            if (roles.getName().equals(roleName)) {
                account.removeRole(role);
                return accountMokRepository.saveAndFlush(account);
            }
        }
        throw new RoleCanNotBeRemoved(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account setAccountStatus(UUID id, boolean status) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setActive(status);
        return accountMokRepository.saveAndFlush(account);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account updateAccountData(UUID id, Account account, String eTag) throws AccountNotFoundException, OptLockException {
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setGender(account.getGender());
        return accountMokRepository.saveAndFlush(accountToUpdate);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getParticipants() throws RoleNotFoundException {
        Role role =
                roleRepository.findByName(AccountRoleEnum.ROLE_PARTICIPANT)
                        .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getManagers() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.ROLE_MANAGER)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAdmins() throws RoleNotFoundException {
        Role role =
                roleRepository.findByName(AccountRoleEnum.ROLE_ADMIN).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountById(UUID id) throws AccountNotFoundException {
        return accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public CredentialReset resetPasswordAndSendEmail(String email) {
        Optional<Account> account = accountMokRepository.findByEmail(email);
        if (account.isEmpty()) {
            return null;
        }
        resetCredentialRepository.deleteByAccount(account.get());
        resetCredentialRepository.flush();
        return verifier.saveTokenToResetCredential(account.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public CredentialReset changePasswordByAdminAndSendEmail(String email) throws AccountNotFoundException {
        Account account = accountMokRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        resetCredentialRepository.deleteByAccount(account);
        resetCredentialRepository.flush();
        return verifier.saveTokenToResetCredential(account);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public ChangeEmail sendMailWhenEmailChangeByAdmin(UUID id, String email) throws AccountNotFoundException, EmailAlreadyExistsException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        if (accountMokRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_EXISTS);
        }
        changeEmailRepository.deleteByAccount(account);
        changeEmailRepository.flush();
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = config.getCredentialChangeTokenExpiration();
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeEmail(randString, account,
                expirationDate, email);
        changeEmailRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPasswordWithToken(String token, String newPassword)
            throws AccountNotFoundException, TokenExpiredException, ThisPasswordAlreadyWasSetInHistory, TokenNotFoundException,
            AccountLockedException, AccountNotVerifiedException {
        Account accountToUpdate = verifier.verifyCredentialReset(token, resetCredentialRepository);
        if (verifier.isPasswordInHistory(accountToUpdate.getId(), newPassword)) {
            throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
        }
        accountToUpdate.setPassword(passwordEncoder.encode(newPassword));
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(accountToUpdate));
        accountMokRepository.saveAndFlush(accountToUpdate);
        resetCredentialRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredResetCredentialTokens() {
        resetCredentialRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredChangePasswordTokens() {
        changeMyPasswordRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public void deleteExpiredChangeEmailTokens() {
        changeEmailRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }


}


