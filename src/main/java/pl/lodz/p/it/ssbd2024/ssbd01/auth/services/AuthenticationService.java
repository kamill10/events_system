package pl.lodz.p.it.ssbd2024.ssbd01.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.dto.LoginRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.dto.RegisterUserRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.UserRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.AccountService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public String registerUser(RegisterUserRequest request) {
        Account account = new Account(request.getUsername(), request.getPassword(), request.getEmail()
                , request.getGender(), request.getFirstName(), request.getLastName());
        accountService.addUser(account);
        return jwtService.generateToken(account);
    }

    public String authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername());
        return jwtService.generateToken(user);
    }

}
