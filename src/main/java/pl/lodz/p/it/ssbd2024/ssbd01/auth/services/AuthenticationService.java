package pl.lodz.p.it.ssbd2024.ssbd01.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repositories.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.request.LoginRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.request.RegisterUserRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.AccountService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final AccountAuthRepository accountAuthRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(RegisterUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Account account = new Account(request.getUsername(), encodedPassword, request.getEmail(), request.getGender(),
                request.getFirstName(), request.getLastName());
        accountService.addAccount(account);
        return jwtService.generateToken(account);
    }

    public String authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = accountAuthRepository.findByUsername(request.getUsername());
        return jwtService.generateToken(user);
    }

}
