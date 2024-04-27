package pl.lodz.p.it.ssbd2024.ssbd01.auth.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountConfirmation;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountConfirmationRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountMokRepository accountMokRepository;
    private final AccountAuthRepository accountAuthRepository;
    private final AccountConfirmationRepository accountConfirmationRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void registerUser(Account account) {
        accountMokRepository.save(account);
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expirationDate = LocalDateTime.now().plusHours(24);
        var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);
        // TODO: Send mail to user with confirmation link

        accountConfirmationRepository.save(newAccountConfirmation);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void verifyAccount(String token) throws AccountConfirmationTokenNotFoundException, AccountConfirmationTokenExpiredException {
        var accountConfirmation = accountConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new AccountConfirmationTokenNotFoundException(ExceptionMessages.CONFIRMATION_TOKEN_NOT_FOUND));
        if (accountConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AccountConfirmationTokenExpiredException(ExceptionMessages.CONFIRMATION_TOKEN_EXPIRED);
        }
        accountConfirmation.getAccount().setVerified(true);
        accountConfirmationRepository.delete(accountConfirmation);
    }

}
