package pl.lodz.p.it.ssbd2024.ssbd01.mok.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.RoleRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMokRepository accountMokRepository;
    private final RoleRepository roleRepository;
    public List<Account> getAllUsers(){
        return accountMokRepository.findAll();
    }
    @Transactional
    public Account addUser(Account account) {
        return accountMokRepository.save(account);
    }
    public Account addRoleToAccount(UUID id, String roleName){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for id: " + id));
        List<Role> accountRoles = account.getRoles();
        if(accountRoles.size() == 1) {
            Role currentRole = accountRoles.getFirst();
            if (currentRole.getName().equals("ADMIN")) {
                if (roleName.equals("PARTICIPANT")) {
                    throw new IllegalArgumentException("ADMIN cannot have the role PARTICIPANT");
                } else if (roleName.equals("ADMIN")) {
                    throw new IllegalArgumentException("This account already has the role ADMIN");
                }
            } else if (currentRole.getName().equals("MANAGER")) {
                if (roleName.equals("PARTICIPANT")) {
                    throw new IllegalArgumentException("MANAGER cannot have the role PARTICIPANT");
                } else if (roleName.equals("MANAGER")) {
                    throw new IllegalArgumentException("This account already has the role MANAGER");
                }
            } else if (currentRole.getName().equals("PARTICIPANT") &&
                    (roleName.equals("ADMIN") || roleName.equals("MANAGER"))) {
                throw new IllegalArgumentException("PARTICIPANT cannot have any other role");
            }
        } else if (accountRoles.size() == 2) {
            throw new IllegalArgumentException("This account has the maximum number of roles");
        }
        account.addRole(role);
        return accountMokRepository.save(account);
    }
    public Account takeRole(UUID id, String roleName){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for id: " + id));
        for(Role roles : account.getRoles()){
            if(roles.getName().equals(roleName)){
                account.removeRole(role);
                return accountMokRepository.save(account);
            }
        }
        throw new IllegalArgumentException("This account does not have role "+roleName);
    }

    public Account setAccountStatus(UUID id, boolean status){
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for id: " + id));
        account.setActive(status);
        return accountMokRepository.save(account);
    }


}