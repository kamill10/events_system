package pl.lodz.p.it.ssbd2024.ssbd01.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.MailToVerifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.RoleNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountDTOConverter accountDTOConverter;
    private final MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateAccountDTO request) throws AccountNotFoundException {
        MailToVerifyDTO mailToVerifyDTO = authenticationService.registerUser(accountDTOConverter.toAccount(request));
        mailService.sendEmailToVerifyAccount(mailToVerifyDTO.account(), mailToVerifyDTO.token());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody LoginDTO request,@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) {
        String token = authenticationService.authenticate(request, language);
        mailService.sendEmailToInformAboutAccountBlocked(authenticationService.getAccountByUsername(request.username()));
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/verify-account/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token)
            throws AccountConfirmationTokenNotFoundException, AccountConfirmationTokenExpiredException, AccountNotFoundException,
            RoleNotFoundException {
        Account account = authenticationService.verifyAccount(token);
        mailService.sendEmailToInformAboutVerification(account);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        authenticationService.logout(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}