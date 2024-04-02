package pl.lodz.p.it.ssbd2024.ssbd01.services.mauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok.UserRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.request.mauth.LoginRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.request.mauth.RegisterUserRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.services.mok.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService accountService;
    private final UserRepository accountRepository;
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
        var user = accountRepository.findByUsername(request.getUsername());
        return jwtService.generateToken(user);
    }

}
