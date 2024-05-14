package pl.lodz.p.it.ssbd2024.ssbd01.auth.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.RoleNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountDTOConverter accountDTOConverter;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateAccountDTO request) {
        authenticationService.registerUser(accountDTOConverter.toAccount(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody LoginDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }

    @PostMapping("/verify-account/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token)
            throws AccountConfirmationTokenNotFoundException, AccountConfirmationTokenExpiredException, AccountNotFoundException,
            RoleNotFoundException {
        authenticationService.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_PARTICIPANT"})
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        authenticationService.logout(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}