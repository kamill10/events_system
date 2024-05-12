package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.CredentialResetByAdmin;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.PasswordHistory;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.CredentialResetAdminRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.PasswordHistoryRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.RoleRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMokRepository accountMokRepository;
    private final RoleRepository roleRepository;
    private final CredentialResetAdminRepository credentialResetAdminRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final PasswordHistoryRepository passwordHistoryRepository;


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addAccount(Account account) {
        Account returnedAccount = accountMokRepository.saveAndFlush(account);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(returnedAccount));
        return returnedAccount;
    }

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
            case PARTICIPANT:
                canAddParticipantRole(account);
                break;
            case MANAGER, ADMIN:
                canAddManagerOrAdminRole(account);
                break;
            default:
                throw new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND);
        }
        account.addRole(role);
        mailService.sendEmail(account, "mail.role.added.subject", "mail.role.added.body", new Object[] {roleName.name()});
        return accountMokRepository.saveAndFlush(account);
    }

    private void canAddManagerOrAdminRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(AccountRoleEnum.PARTICIPANT))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    private void canAddParticipantRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.isEmpty()) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account removeRole(UUID id, AccountRoleEnum roleName) throws RoleNotFoundException, AccountNotFoundException, RoleCanNotBeRemoved {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        for (Role roles : account.getRoles()) {
            if (roles.getName().equals(roleName)) {
                account.removeRole(role);
                mailService.sendEmail(account, "mail.role.removed.subject", "mail.role.removed.body", new Object[] {roleName.name()});
                return accountMokRepository.saveAndFlush(account);
            }
        }
        throw new RoleCanNotBeRemoved(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account setAccountStatus(UUID id, boolean status) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setActive(status);
        if (status) {
            mailService.sendEmail(account, "mail.unblocked.subject", "mail.unblocked.body", new Object[] {});
        } else {
            mailService.sendEmail(account, "mail.blocked.subject", "mail.blocked.body", new Object[] {});
        }
        return accountMokRepository.saveAndFlush(account);
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

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


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getParticipants() throws RoleNotFoundException {
        Role role =
                roleRepository.findByName(AccountRoleEnum.PARTICIPANT).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getManagers() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.MANAGER).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAdmins() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.ADMIN).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountById(UUID id) throws AccountNotFoundException {
        return accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPasswordAndSendEmail(String email) {
        Optional<Account> account = accountMokRepository.findByEmail(email);
        if (account.isEmpty()) {
            return;
        }
        CredentialResetByAdmin credentialResetByAdmin = saveTokenToChangeCredential(account.get());
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialResetByAdmin.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(credentialResetByAdmin.getAccount(), "mail.password.reset.subject",
                "mail.password.reset.body", new Object[] {sb});
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String changePasswordAndSendEmail(String email) throws AccountNotFoundException {
        Account account = accountMokRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        CredentialResetByAdmin credentialResetByAdmin = saveTokenToChangeCredential(account);
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialResetByAdmin.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(credentialResetByAdmin.getAccount(), "mail.password.changed.by.admin.subject",
                "mail.password.changed.by.admin.body", new Object[] {sb});
        return credentialResetByAdmin.getToken();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String sendMailWhenEmailChange(UUID id, String email) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        CredentialResetByAdmin credentialResetByAdmin = saveTokenToChangeCredential(account);
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/change-email?token=");
        sb.append(credentialResetByAdmin.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(credentialResetByAdmin.getAccount(), "mail.email.changed.by.admin.subject",
                "mail.email.changed.by.admin.body", new Object[] {sb}, email);
        return credentialResetByAdmin.getToken();

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPasswordWithToken(String token, String newPassword)
            throws AccountNotFoundException, PasswordTokenExpiredException, ThisPasswordAlreadyWasSetInHistory {
        Account accountToUpdate = verifyCredentialReset(token);
        if (isPasswordInHistory(accountToUpdate, newPassword)) {
            throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
        }
        accountToUpdate.setPassword(passwordEncoder.encode(newPassword));
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(accountToUpdate));
        accountMokRepository.saveAndFlush(accountToUpdate);
        credentialResetAdminRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeEmailWithToken(String token, String newEmail)
            throws PasswordTokenExpiredException, AccountNotFoundException, EmailAlreadyExistsException {
        if (accountMokRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_EXISTS);
        }
        Account accountToUpdate = verifyCredentialReset(token);
        accountToUpdate.setEmail(newEmail);
        accountMokRepository.saveAndFlush(accountToUpdate);
        credentialResetAdminRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account verifyCredentialReset(String token) throws AccountNotFoundException, PasswordTokenExpiredException {
        Optional<CredentialResetByAdmin> credentialReset = credentialResetAdminRepository.findByToken(token);
        if (credentialReset.isEmpty()) {
            throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND);
        }
        if (credentialReset.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new PasswordTokenExpiredException(ExceptionMessages.PASS_TOKEN_EXPIRED);
        }
        return accountMokRepository.findByEmail(credentialReset.get().getAccount().getEmail())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void logSwitchRole(UUID accountId, AccountRoleEnum roleEnum) throws AccountNotFoundException, RoleNotAssignedToAccount {
        Account account = accountMokRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.contains(new Role(roleEnum))) {
            throw new RoleNotAssignedToAccount(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
        }
    }

    private boolean isPasswordInHistory(Account account, String password) {
        return passwordHistoryRepository.findPasswordHistoryByAccount(account)
                .stream().anyMatch(passwordHistory -> passwordEncoder.matches(password, passwordHistory.getPassword()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public CredentialResetByAdmin saveTokenToChangeCredential(Account account) {
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("credential_change.token.expiration.minutes")));
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new CredentialResetByAdmin(randString, account, expirationDate);
        credentialResetAdminRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Scheduled(fixedRate = 120000)
    public void deleteExiredTokens() {
        credentialResetAdminRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }


}


