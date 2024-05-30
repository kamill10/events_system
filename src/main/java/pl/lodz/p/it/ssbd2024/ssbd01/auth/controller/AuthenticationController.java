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
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.auth.AccountConfirmationTokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountDTOConverter accountDTOConverter;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody CreateAccountDTO request) {
        authenticationService.registerAccount(accountDTOConverter.toAccount(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> authenticate(@Valid @RequestBody LoginDTO request, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language)
            throws AppException {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request, language));
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<String> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)
            throws AppException {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshJWT(token));
    }

    @PostMapping("/verify-account/{token}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> verifyAccount(@PathVariable String token)
            throws AppException {
        authenticationService.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        authenticationService.logout(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/unblock-account/{token}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> unlockAccount(@PathVariable String token)
            throws AppException {
        authenticationService.unlockAccountThatWasNotUsed(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}