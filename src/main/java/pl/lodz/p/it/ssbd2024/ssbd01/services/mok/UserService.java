package pl.lodz.p.it.ssbd2024.ssbd01.services.mok;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok.RoleRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
