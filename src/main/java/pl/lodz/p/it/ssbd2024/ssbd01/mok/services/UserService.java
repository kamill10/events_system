package pl.lodz.p.it.ssbd2024.ssbd01.mok.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.RoleRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.UserRepository;

import java.util.List;

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
        //user get the role GUEST on the start,toCHANGE
        //if role guest not exist, create it
        if(roleRepository.findByName("GUEST") == null) {
            Role role = new Role("GUEST");
            roleRepository.save(role);
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRoles(List.of(roleRepository.findByName("GUEST")));
        return userRepository.save(account);
    }


}
