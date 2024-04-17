package pl.lodz.p.it.ssbd2024.ssbd01.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repositories.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
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

    public String registerUser(CreateAccountDTO createAccountDTO) {
        Account account = new Account(
                createAccountDTO.username(),
                passwordEncoder.encode(createAccountDTO.password()),
                createAccountDTO.email(),
                createAccountDTO.gender(),
                createAccountDTO.firstName(),
                createAccountDTO.lastName());
        accountService.addAccount(account);
        return jwtService.generateToken(account);
    }

    public String authenticate(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );
        var user = accountAuthRepository.findByUsername(loginDTO.username());
        return jwtService.generateToken(user);
    }

}
