package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountPersonalDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateMyEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateMyPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.MeService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;


@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {
    private final MeService meService;
    private final AccountDTOConverter accountDTOConverter;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<GetAccountPersonalDTO> getMyAccount() throws NotFoundException {
        Account accountToReturn = meService.getAccount();
        String eTag = ETagBuilder.buildETag(accountToReturn.getVersion().toString());
        GetAccountPersonalDTO accountDto = accountDTOConverter.toAccountPersonalDTO(accountToReturn);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, eTag).body(accountDto);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<String> changeMyPasswordSendEmail(@RequestBody UpdateMyPasswordDTO updateMyPasswordDto)
            throws AccountNotFoundException, WrongOldPasswordException, ThisPasswordAlreadyWasSetInHistory {
        return ResponseEntity.status(HttpStatus.OK).body(
                meService.changeMyPasswordSendMail(updateMyPasswordDto.oldPassword(), updateMyPasswordDto.newPassword())
        );
    }

    @PostMapping("/email")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<String> changeMyEmailSendEmail(@RequestBody UpdateMyEmailDTO updateMyEmailDTO)
            throws AccountNotFoundException, WrongOldPasswordException {
        return ResponseEntity.status(HttpStatus.OK).body(
                meService.changeMyEmailSendMail(updateMyEmailDTO.password(), updateMyEmailDTO.newEmail())
        );
    }

    @PatchMapping("/change-password/token/{token}")
    public ResponseEntity<?> changePasswordWithToken(@PathVariable String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException {
        meService.changeMyPasswordWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/email/token/{token}")
    public ResponseEntity<?> changeEmailWithToken(@PathVariable String token)
            throws AccountNotFoundException, TokenExpiredException, TokenNotFoundException {
        meService.changeMyEmailWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PutMapping("/user-data")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<GetAccountPersonalDTO> updateMyData(
            @RequestHeader("If-Match") String eTag,
            @RequestBody UpdateAccountDataDTO updateAccountDataDTO
    ) throws AccountNotFoundException, OptLockException {
        return ResponseEntity.status(HttpStatus.OK).body(accountDTOConverter.toAccountPersonalDTO(
                meService.updateMyAccountData(accountDTOConverter.toAccount(updateAccountDataDTO), eTag)));
    }

    @PostMapping("/switch-role")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<?> logSwitchRole(@RequestParam AccountRoleEnum role) throws AccountNotFoundException, RoleNotAssignedToAccount {
        meService.logSwitchRole(role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}