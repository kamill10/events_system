package pl.lodz.p.it.ssbd2024.ssbd01.mok.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.RoleRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public List<Account> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public Account addUser(Account account) {
        return userRepository.save(account);
    }
    public Account addRoleToAccount(UUID id, String roleName){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        Account account = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for id: " + id));
        if(account.getRoles().isEmpty()){
            account.addRole(role);
            return userRepository.save(account);
        }
        else if(account.getRoles().size() == 2){
            throw new IllegalArgumentException("This account has maximum amount of roles");
        }
        else if (Objects.equals(account.getRoles().getFirst().getName(), "ADMIN")
        && Objects.equals(roleName, "PARTICIPANT")) {
            throw new IllegalArgumentException("ADMIN cannot has role PARTICIPANT");
        }
        if (Objects.equals(account.getRoles().getFirst().getName(), "ADMIN")
                && Objects.equals(roleName, "ADMIN")) {
            throw new IllegalArgumentException("This account alread has role ADMIN");
        }
        else if (Objects.equals(account.getRoles().getFirst().getName(), "MANAGER")
                && Objects.equals(roleName, "PARTICIPANT")) {
            throw new IllegalArgumentException("MANAGER cannot has role PARTICIPANT");
        }
        else if (Objects.equals(account.getRoles().getFirst().getName(), "MANAGER")
                && Objects.equals(roleName, "MANAGER")) {
            throw new IllegalArgumentException("This account alread has role MANAGER");
        }
        else if (Objects.equals(account.getRoles().getFirst().getName(), "PARTICIPANT")&&(
                 Objects.equals(roleName, "ADMIN")
                ||  Objects.equals(roleName, "MANAGER"))) {
            throw new IllegalArgumentException("PARTICIPANT cannot has any other role");
        }

        else{
            account.addRole(role);
            return userRepository.save(account);
        }
    }
    public Account takeRole(UUID id, String roleName){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        Account account = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for id: " + id));
        for(Role roles : account.getRoles()){
            if(roles.getName().equals(roleName)){
                account.removeRole(role);
                return userRepository.save(account);
            }
        }
        throw new IllegalArgumentException("This account does not have role "+roleName);
    }


}
