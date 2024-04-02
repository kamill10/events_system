package pl.lodz.p.it.ssbd2024.ssbd01.services.mok;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public List<Account> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public Account addUser(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return userRepository.save(account);
    }


}
