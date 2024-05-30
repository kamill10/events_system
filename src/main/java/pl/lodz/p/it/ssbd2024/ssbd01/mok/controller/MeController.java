package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.ThemeDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountPersonalDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateMyEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateMyPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
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
    public ResponseEntity<GetAccountPersonalDTO> getMyAccount() throws AppException {
        Account accountToReturn = meService.getAccount();
        String eTag = ETagBuilder.buildETag(accountToReturn.getVersion().toString());
        GetAccountPersonalDTO accountDto = accountDTOConverter.toAccountPersonalDTO(accountToReturn);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, eTag).body(accountDto);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<?> changeMyPasswordSendEmail(@Valid @RequestBody UpdateMyPasswordDTO updateMyPasswordDto)
            throws AppException {
        meService.changeMyPasswordSendMail(updateMyPasswordDto.oldPassword(), updateMyPasswordDto.newPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/email")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<?> changeMyEmailSendEmail(@Valid @RequestBody UpdateMyEmailDTO updateMyEmailDTO)
            throws AppException {
        meService.changeMyEmailSendMail(updateMyEmailDTO.password(), updateMyEmailDTO.newEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-password/token/{token}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> changePasswordWithToken(@PathVariable String token)
            throws AppException {
        meService.changeMyPasswordWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-email/token/{token}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> changeEmailWithToken(@PathVariable String token)
            throws AppException {
        meService.changeMyEmailWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PutMapping("/user-data")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<GetAccountPersonalDTO> updateMyData(
            @RequestHeader("If-Match") String eTag,
            @Valid @RequestBody UpdateAccountDataDTO updateAccountDataDTO
    ) throws AppException {
        return ResponseEntity.status(HttpStatus.OK).body(accountDTOConverter.toAccountPersonalDTO(
                meService.updateMyAccountData(accountDTOConverter.toAccount(updateAccountDataDTO), eTag, updateAccountDataDTO.theme(),
                        updateAccountDataDTO.timeZone())));
    }

    @PostMapping("/switch-role")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<?> logSwitchRole(@RequestParam AccountRoleEnum role) throws AppException {
        meService.logSwitchRole(role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/user-data/theme")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<?> setMyTheme(@Valid @RequestBody ThemeDTO theme) throws AppException {
        meService.setAccountTheme(theme.theme());
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}