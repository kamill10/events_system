package pl.lodz.p.it.ssbd2024.ssbd01.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.request.LoginRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.request.RegisterUserRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories.UserRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService accountService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(RegisterUserRequest request) {
        Account account = new Account(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail()
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
