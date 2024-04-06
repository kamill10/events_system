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
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public List<Account> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public Account addUser(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Role role = roleRepository.findByName("PARTICIPANT")
                .orElseThrow(() -> new IllegalArgumentException("Role not found: PARTICIPANT"));
        account.addRole(role);
        return userRepository.save(account);
    }
    public Account addRoleToAccount(UUID id, String roleName){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        Account account = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for id: " + id));
        account.addRole(role);
        //todo Handle the exceptions ,for example when ADMIN is going to get PARTICIPANT role
        userRepository.save(account);
        return account;
    }


}
