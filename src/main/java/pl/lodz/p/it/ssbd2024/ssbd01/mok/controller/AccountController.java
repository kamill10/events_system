package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.ThemeDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.TimeZoneDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.get.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final AccountDTOConverter accountDTOConverter;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GetAccountDTO> getAllAccounts() {
        List<GetAccountDTO> getAccountDTOS = accountDTOConverter.accountDtoList(accountService.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<GetAccountDTO>> getFilteredAccounts(
            @RequestParam(required = false) String phrase,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String key
    ) {
        GetAccountPageDTO getAccountPageDTO = new GetAccountPageDTO(page, size, direction, key, phrase);
        Page<GetAccountDTO> getAccountDTOPage = accountDTOConverter.accountDTOPage(accountService.getAccountsPage(getAccountPageDTO));
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOPage);
    }

    @PostMapping("/{id}/add-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAccountDTO> addRoleToAccount(@RequestHeader(HttpHeaders.IF_MATCH) String eTagReceived,
                                                          @PathVariable UUID id, @RequestParam AccountRoleEnum roleName)
            throws AppException {
        var account = accountService.addRoleToAccount(id, roleName,eTagReceived);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.IF_MATCH,eTag)
                .body(accountDTOConverter.toAccountDto(account));
    }

    @DeleteMapping("/{id}/remove-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAccountDTO> removeRole(@RequestHeader(HttpHeaders.IF_MATCH) String eTagReceived,
                                                    @PathVariable UUID id, @RequestParam AccountRoleEnum roleName)
            throws AppException {
        var account = accountService.removeRoleFromAccount(id, roleName,eTagReceived);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.IF_MATCH,eTag)
                .body(accountDTOConverter.toAccountDto(account));
    }

    @PatchMapping("/{id}/set-active")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAccountDTO> setActive(@RequestHeader(HttpHeaders.IF_MATCH) String eTagReceived,
                                                   @PathVariable UUID id) throws AppException {
        var account = accountService.setAccountStatus(id, true, eTagReceived);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.IF_MATCH,eTag)
                .body(accountDTOConverter.toAccountDto(account));
    }

    @PatchMapping("/{id}/set-inactive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAccountDTO> setInactive(@RequestHeader(HttpHeaders.IF_MATCH) String eTagReceived,
                                                     @PathVariable UUID id) throws AppException {
        var account = accountService.setAccountStatus(id, false, eTagReceived);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.IF_MATCH,eTag)
                .body(accountDTOConverter.toAccountDto(account));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAccountDetailedDTO> getAccountByUsername(@PathVariable String username)
            throws AppException {
        Account account = accountService.getAccountByUsername(username);
        GetAccountDetailedDTO accountDto = accountDTOConverter.toAccountDetailedDTO(account);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG, eTag)
                .body(accountDto);
    }


    @PutMapping("/{id}/user-data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAccountPersonalDTO> updateAccountData(
            @RequestHeader("If-Match") String eTag,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAccountDataDTO updateAccountDataDTO
    ) throws AppException {
        GetAccountPersonalDTO updatedAccount = accountDTOConverter.toAccountPersonalDTO(
                accountService.updateAccountData(id, accountDTOConverter.toAccount(updateAccountDataDTO), eTag));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @GetMapping("/participants")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GetAccountDTO>> getParticipants() throws AppException {
        List<GetAccountDTO> participants = accountDTOConverter.accountDtoList(accountService.getParticipants());
        return ResponseEntity.status(HttpStatus.OK).body(participants);
    }

    @GetMapping("/administrators")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GetAccountDTO>> getAdministrators() throws AppException {
        List<GetAccountDTO> administrators = accountDTOConverter.accountDtoList(accountService.getAdmins());
        return ResponseEntity.status(HttpStatus.OK).body(administrators);
    }

    @GetMapping("/managers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GetAccountDTO>> getManagers() throws AppException {
        List<GetAccountDTO> managers = accountDTOConverter.accountDtoList(accountService.getManagers());
        return ResponseEntity.status(HttpStatus.OK).body(managers);
    }

    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UpdateEmailDTO emailDTO) {
        accountService.resetPasswordAndSendEmail(emailDTO.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/reset-password/token/{token}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPasswordWithToken(@PathVariable String token, @Valid @RequestBody UpdatePasswordDTO password)
            throws AppException {
        accountService.resetPasswordWithToken(token, password.value());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UpdateEmailDTO emailDTO) throws AppException {
        accountService.changePasswordByAdminAndSendEmail(emailDTO.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-email/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeEmail(@PathVariable UUID id, @Valid @RequestBody UpdateEmailDTO emailDTO)
            throws AppException {
        accountService.sendMailWhenEmailChangeByAdmin(id, emailDTO.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/history/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GetAccountHistoryDetailedDTO>> getAccountHistoryListByUsername(@PathVariable String username) {
        List<GetAccountHistoryDetailedDTO> accountHistoryList =
                accountDTOConverter.accountHistoryDtoList(accountService.getAllAccountHistoryByUsername(username));
        return ResponseEntity.status(HttpStatus.OK).body(accountHistoryList);
    }

    @PostMapping("time-zone")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<TimeZoneDTO> addTimeZone(@RequestBody TimeZoneDTO timeZone) throws AppException {
        accountService.addTimeZone(timeZone.timeZone());
        return ResponseEntity.status(HttpStatus.OK).body(timeZone);
    }

    @PostMapping("theme")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ThemeDTO> addTheme(@RequestBody ThemeDTO theme) {
        accountService.addTheme(theme.theme());
        return ResponseEntity.status(HttpStatus.OK).body(theme);
    }
}