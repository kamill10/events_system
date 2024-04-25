package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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


    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }

    @Transactional
    public Account addAccount(Account account) {
        return accountMokRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account addRoleToAccount(UUID id, String roleName)
            throws RoleAlreadyAssignedException, AccountRolesLimitExceedException, WrongRoleToAccountException, RoleNotFoundException,
            AccountNotFoundException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(role)) {
            throw new RoleAlreadyAssignedException(ExceptionMessages.ROLE_ALREADY_ASSIGNED);
        }
        if (accountRoles.size() == 2) {
            throw new AccountRolesLimitExceedException(ExceptionMessages.ACCOUNT_ROLES_LIMIT_EXCEEDED);
        }
        if (accountRoles.contains(new Role("PARTICIPANT"))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
        if (!accountRoles.isEmpty() && role.equals(new Role("PARTICIPANT"))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);

        }
        account.addRole(role);
        return accountMokRepository.save(account);
    }

    @Transactional
    public Account removeRole(UUID id, String roleName) throws RoleNotFoundException, AccountNotFoundException, RoleCanNotBeRemoved {
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

    @Transactional
    public Account setAccountStatus(UUID id, boolean status) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setActive(status);
        return accountMokRepository.save(account);
    }


    @Transactional
    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account updateAccountData(UUID id, Account account) throws AccountNotFoundException {
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setGender(account.getGender());
        return accountMokRepository.save(accountToUpdate);
    }

    @Transactional
    public List<Account> getParticipants() throws ParticipantNotFoundException {
        List<Account> participants = getAllAccounts()
                .stream()
                .filter(account -> account.getRoles().contains(new Role("PARTICIPANT")))
                .toList();
        if (participants.isEmpty()) {
            throw new ParticipantNotFoundException(ExceptionMessages.NO_PARTICIPANTS_FOUND);
        }
        return participants;
    }

    @Transactional
    public List<Account> getManagers() throws AccountNotFoundException {
        List<Account> moderators = getAllAccounts()
                .stream()
                .filter(account -> account.getRoles().contains(new Role("MANAGER")))
                .toList();
        if (moderators.isEmpty()) {
            throw new AccountNotFoundException(ExceptionMessages.NO_MANAGERS_FOUND);
        }
        return moderators;
    }

    @Transactional
    public List<Account> getAdmins() throws AdminNotFoundException {
        List<Account> admins = getAllAccounts()
                .stream()
                .filter(account -> account.getRoles().contains(new Role("ADMIN")))
                .toList();
        if (admins.isEmpty()) {
            throw new AdminNotFoundException(ExceptionMessages.NO_ADMINS_FOUND);
        }
        return admins;
    }

    @Transactional
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

    public Account getAccountById(UUID id) throws AccountNotFoundException {
        return accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePassword(UUID id, String password) throws AccountNotFoundException {
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        accountToUpdate.setPassword(passwordEncoder.encode(password));
        accountMokRepository.saveAndFlush(accountToUpdate);
    }

    @Transactional
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

    @Transactional
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
