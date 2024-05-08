package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.ThisPasswordAlreadyWasSetInHistory;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;


@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {
    private final AccountService accountService;
    private final AccountDTOConverter accountDTOConverter;

    @GetMapping
    public ResponseEntity<GetAccountDTO> getMyAccount() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        GetAccountDTO accountDto = accountDTOConverter.toAccountDto(accountService.getAccountById(account.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }


    @PatchMapping("/email")
    public ResponseEntity<GetAccountDTO> updateMyEmail(@RequestBody UpdateEmailDTO email)
            throws AccountNotFoundException, EmailAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountDTOConverter.toAccountDto(accountService.updateAccountEmail(account.getId(), email.email())));
    }


    @PatchMapping("/password")
    public ResponseEntity<GetAccountDTO> updateMyAccountPassword(@RequestBody UpdatePasswordDTO password)
            throws AccountNotFoundException, ThisPasswordAlreadyWasSetInHistory {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        accountService.updatePassword(account.getId(), password.value());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/user-data")
    public ResponseEntity<GetAccountDTO> updateMyData(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                      @RequestBody UpdateAccountDataDTO updateAccountDataDTO)
            throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(accountDTOConverter.toAccountDto(
                accountService.updateAccountData(account.getId(), accountDTOConverter.toAccount(updateAccountDataDTO))));
    }
}
