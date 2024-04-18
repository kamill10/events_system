package pl.lodz.p.it.ssbd2024.ssbd01.mok.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.RoleRepository;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMokRepository accountMokRepository;
    private final RoleRepository roleRepository;

    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }
    @Transactional
    public Account addAccount(Account account) {
        return accountMokRepository.save(account);
    }
    public Account addRoleToAccount(UUID id, String roleName) throws RoleAlreadyAssignedException, AccountRolesLimitExceedException, WrongRoleToAccountException, RoleNotFoundException, AccountNotFoundException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->  new RoleNotFoundException( ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException( ExceptionMessages.ACCOUNT_NOT_FOUND ));
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
    public Account removeRole(UUID id, String roleName) throws RoleNotFoundException, AccountNotFoundException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->  new RoleNotFoundException( ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException( ExceptionMessages.ACCOUNT_NOT_FOUND ));
        for (Role roles : account.getRoles()) {
            if (roles.getName().equals(roleName)) {
                account.removeRole(role);
                return accountMokRepository.save(account);
            }
        }
        throw new IllegalArgumentException("This account does not have role " + roleName);
    }

    public Account setAccountStatus(UUID id, boolean status) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException( ExceptionMessages.ACCOUNT_NOT_FOUND ));
        account.setActive(status);
        return accountMokRepository.save(account);
    }


    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException( ExceptionMessages.ACCOUNT_NOT_FOUND ));
    }

    public Account updateAccountUserData(UUID id, Account account) throws AccountNotFoundException {
        Account accountToUpdate = accountMokRepository.findById(id)
                 .orElseThrow(() -> new AccountNotFoundException( ExceptionMessages.ACCOUNT_NOT_FOUND ));
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setEmail(account.getEmail());
        accountToUpdate.setGender(account.getGender());
        return accountMokRepository.save(accountToUpdate);
    }
    public List<Account> getParticipants() throws ParticipantNotFoundException {
        List<Account> participants= getAllAccounts()
                .stream()
                .filter(account -> account.getRoles().contains(new Role("PARTICIPANT")))
                .toList();
        if(participants.isEmpty()){
            throw new ParticipantNotFoundException(ExceptionMessages.NO_PARTICIPANTS_FOUND);
        }
        return participants;
    }
    public List<Account> getManagers() throws AccountNotFoundException {
        List<Account>moderators = getAllAccounts()
                .stream()
                .filter(account -> account.getRoles().contains(new Role("MANAGER")))
                .toList();
        if(moderators.isEmpty()){
            throw new AccountNotFoundException(ExceptionMessages.NO_MANAGERS_FOUND);
        }
        return moderators;
    }
    public List<Account> getAdmins() throws AdminNotFoundException {
        List<Account>admins = getAllAccounts()
                .stream()
                .filter(account -> account.getRoles().contains(new Role("ADMIN")))
                .toList();
        if (admins.isEmpty()){
            throw new AdminNotFoundException(ExceptionMessages.NO_ADMINS_FOUND);
        }
        return admins;
    }

}
