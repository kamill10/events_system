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
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateMyEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateMyPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;


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
        Account accountToReturn = accountService.getAccountById(account.getId());
        String eTag = ETagBuilder.buildETag(accountToReturn.getVersion().toString());
        GetAccountDTO accountDto = accountDTOConverter.toAccountDto(accountToReturn);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, eTag).body(accountDto);
    }


    @PatchMapping("/email")
    public ResponseEntity<GetAccountDTO> updateMyEmail(@RequestBody UpdateEmailDTO email)
            throws AccountNotFoundException, EmailAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changeMyPasswordSendEmail(@RequestBody UpdateMyPasswordDTO updateMyPasswordDto)
            throws AccountNotFoundException, ThisPasswordAlreadyWasSetInHistory, WrongOldPasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(
                accountService.changeMyPasswordSendMail(account.getId(),updateMyPasswordDto.oldPassword(),
                        updateMyPasswordDto.newPassword())
        );
    }

    @PostMapping("/change-email")
    public ResponseEntity<String> changeMyEmailSendEmail(@RequestBody UpdateMyEmailDTO updateMyEmailDTO)
            throws AccountNotFoundException, WrongOldPasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(
                accountService.changeMyEmailSendMail(account.getId(), updateMyEmailDTO.password(), updateMyEmailDTO.newEmail())
        );
    }

    @PatchMapping("/change-password/token/{token}")
    public ResponseEntity<?> changePasswordWithToken(@PathVariable String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException {
        accountService.changeMyPasswordWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-email/token/{token}")
    public ResponseEntity<?> changeEmailWithToken(@PathVariable String token)
            throws AccountNotFoundException, TokenExpiredException, TokenNotFoundException {
        accountService.changeMyEmailWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PutMapping("/user-data")
    public ResponseEntity<GetAccountDTO> updateMyData(@RequestHeader("If-Match") String eTag, @RequestBody UpdateAccountDataDTO updateAccountDataDTO)
            throws AccountNotFoundException, OptLockException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(accountDTOConverter.toAccountDto(
                accountService.updateAccountData(account.getId(), accountDTOConverter.toAccount(updateAccountDataDTO), eTag)));
    }

    @PostMapping("/switch-role")
    public ResponseEntity<?> logSwitchRole(@RequestParam AccountRoleEnum role) throws AccountNotFoundException, RoleNotAssignedToAccount {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        accountService.logSwitchRole(account.getId(), role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}