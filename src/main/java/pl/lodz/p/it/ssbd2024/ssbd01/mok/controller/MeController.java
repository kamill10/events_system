package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import jakarta.validation.Valid;
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
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.ThemeEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.TimeZoneEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeEmail;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeMyPassword;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.MeService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;



@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {
    private final MeService meService;
    private final AccountDTOConverter accountDTOConverter;
    private final MailService mailService;

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
    public ResponseEntity<?> changeMyPasswordSendEmail(@RequestBody UpdateMyPasswordDTO updateMyPasswordDto)
            throws WrongOldPasswordException, ThisPasswordAlreadyWasSetInHistory {
        ChangeMyPassword changeMyPassword = meService.changeMyPasswordSendMail(updateMyPasswordDto.oldPassword(), updateMyPasswordDto.newPassword());
        mailService.sendEmailToChangeMyPassword(changeMyPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/email")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<?> changeMyEmailSendEmail(@Valid @RequestBody UpdateMyEmailDTO updateMyEmailDTO)
            throws AccountNotFoundException, WrongOldPasswordException, EmailAlreadyExistsException {
        ChangeEmail changeEmail = meService.changeMyEmailSendMail(updateMyEmailDTO.password(), updateMyEmailDTO.newEmail());
        mailService.sendEmailToChangeMyEmail(changeEmail, updateMyEmailDTO.newEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-password/token/{token}")
    public ResponseEntity<?> changePasswordWithToken(@PathVariable String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException, AccountLockedException, AccountNotVerifiedException {
        meService.changeMyPasswordWithToken(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-email/token/{token}")
    public ResponseEntity<?> changeEmailWithToken(@PathVariable String token)
            throws AccountNotFoundException, TokenExpiredException, TokenNotFoundException, AccountLockedException, AccountNotVerifiedException {
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

    @PatchMapping("/user-data/theme/{theme}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<ThemeEnum> setMyTheme(@PathVariable ThemeEnum theme) throws AccountThemeNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(meService.setAccountTheme(theme));
    }

    @PatchMapping("/user-data/time-zone/{timeZone}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<String> setMyTimeZone(@PathVariable TimeZoneEnum timeZone) throws TimeZoneNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(meService.setAccountTimeZone(timeZone));
    }

    @GetMapping("/theme")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<ThemeEnum> getMyTheme() {
        return ResponseEntity.status(HttpStatus.OK).body(meService.getAccountTheme());
    }

    @GetMapping("/time-zone")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<String> getMyTimeZone() {
        return ResponseEntity.status(HttpStatus.OK).body(meService.getAccountTimeZone());
    }



}