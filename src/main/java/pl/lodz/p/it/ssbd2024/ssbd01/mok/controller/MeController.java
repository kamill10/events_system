package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.util.UUID;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {
    private final AccountService accountService;
    private final AccountDTOConverter AccountDTOConverter;
    private final JwtService jwtService;


    @PatchMapping("/email")
    public ResponseEntity<GetAccountDTO> updateMyEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UpdateEmailDTO email)
            throws AccountNotFoundException, EmailAlreadyExistsException {
        Account account = accountService.getAccountByUsername(jwtService.extractLoginFromHeader(token));
        GetAccountDTO updatedAccount = AccountDTOConverter
                .toAccountDto(accountService.updateAccountEmail(account.getId(), email.email()));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }


    @PatchMapping("/password")
    public ResponseEntity<GetAccountDTO> updateAccountPassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                               @RequestBody UpdatePasswordDTO password)
            throws AccountNotFoundException {
        Account account = accountService.getAccountByUsername(jwtService.extractLoginFromHeader(token));
        accountService.updatePassword(account.getId(), password.value());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
