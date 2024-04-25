package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.PasswordReset;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.PasswordResetRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.RoleRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMokRepository accountMokRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addAccount(Account account) {
        return accountMokRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addRoleToAccount(UUID id, AccountRoleEnum roleName)
            throws RoleAlreadyAssignedException, WrongRoleToAccountException, RoleNotFoundException,
            AccountNotFoundException {
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(role)) {
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
        return accountMokRepository.save(account);
    }

    private boolean canAddManagerOrAdminRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(AccountRoleEnum.PARTICIPANT))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
        return true;
    }

    private boolean canAddParticipantRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.isEmpty()) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
        return true;
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
                return accountMokRepository.save(account);
            }
        }
        throw new RoleCanNotBeRemoved(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account setAccountStatus(UUID id, boolean status) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setActive(status);
        return accountMokRepository.save(account);
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account updateAccountData(UUID id, Account account) throws AccountNotFoundException {
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setGender(account.getGender());
        return accountMokRepository.save(accountToUpdate);
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account updateAccountEmail(UUID id, String email) throws AccountNotFoundException, EmailAlreadyExistsException {
        if (accountMokRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_EXISTS);
        }
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        // TODO: Uncomment this line when we will be sending real mails
        // TODO: Extract emailbody to properties file and use i18n
        accountToUpdate.setEmail(email);
//        mailService.sendEmail(accountToUpdate, "Email change", "Your email has been changed to: " + email);

        return accountMokRepository.save(accountToUpdate);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountById(UUID id) throws AccountNotFoundException {
        return accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePassword(UUID id, String password) throws AccountNotFoundException {
        Account accountToUpdate =
                accountMokRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        accountToUpdate.setPassword(passwordEncoder.encode(password));
        accountMokRepository.saveAndFlush(accountToUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPassword(String email) {
        Optional<Account> accountToUpdate = accountMokRepository.findByEmail(email);
        if (accountToUpdate.isEmpty()) {
            return;
        }
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expirationDate = LocalDateTime.now().plusMinutes(30);
        var newResetIssue = new PasswordReset(randString, email, expirationDate);
        // TODO: Send mail to user with reset link

        passwordResetRepository.save(newResetIssue);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPasswordWithToken(String token, String newPassword) throws PasswordTokenExpiredException, AccountNotFoundException {
        Optional<PasswordReset> passwordReset = passwordResetRepository.findByToken(token);
        if (passwordReset.isEmpty()) {
            throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND);
        }
        if (passwordReset.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new PasswordTokenExpiredException(ExceptionMessages.PASS_TOKEN_EXPIRED);
        }
        Optional<Account> accountToUpdate = accountMokRepository.findByEmail(passwordReset.get().getEmail());
        if (accountToUpdate.isEmpty()) {
            throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND);
        }
        Account account = accountToUpdate.get();
        account.setPassword(newPassword);
        accountMokRepository.save(account);
    }
}
