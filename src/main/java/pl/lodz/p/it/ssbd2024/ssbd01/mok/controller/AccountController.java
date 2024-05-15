package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountPersonalDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.UnprocessableEntityException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;

import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final AccountDTOConverter accountDTOConverter;


    @GetMapping
    public List<GetAccountDTO> getAllUsers() {
        List<GetAccountDTO> getAccountDTOS = accountDTOConverter.accountDtoList(accountService.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
    }

    @PostMapping
    public ResponseEntity<GetAccountPersonalDTO> createUser(@RequestBody CreateAccountDTO createAccountDTO) {
        GetAccountPersonalDTO getAccountDTO = accountDTOConverter.toAccountPersonalDTO(accountService
                .addAccount(accountDTOConverter.toAccount(createAccountDTO)));
        return ResponseEntity.status(HttpStatus.CREATED).body(getAccountDTO);
    }

    @PostMapping("/{id}/add-role")
    public ResponseEntity<GetAccountDTO> addRoleToAccount(@PathVariable UUID id, @RequestParam AccountRoleEnum roleName)
            throws BadRequestException, NotFoundException, ConflictException {
        GetAccountDTO updatedAccount = accountDTOConverter.toAccountDto(accountService.addRoleToAccount(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @DeleteMapping("/{id}/remove-role")
    public ResponseEntity<GetAccountDTO> removeRole(@PathVariable UUID id, @RequestParam AccountRoleEnum roleName)
            throws BadRequestException, NotFoundException {
        GetAccountDTO updatedAccount = accountDTOConverter.toAccountDto(accountService.removeRole(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/set-active")
    public ResponseEntity<GetAccountDTO> setActive(@PathVariable UUID id) throws NotFoundException {
        GetAccountDTO updatedAccount = accountDTOConverter.toAccountDto(accountService.setAccountStatus(id, true));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/set-inactive")
    public ResponseEntity<GetAccountDTO> setInactive(@PathVariable UUID id) throws NotFoundException {
        GetAccountDTO updatedAccount = accountDTOConverter.toAccountDto(accountService.setAccountStatus(id, false));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<GetAccountDetailedDTO> getAccountByUsername(@PathVariable String username,
                                                                      @RequestHeader("The-Timezone-IANA") String timezone)
            throws NotFoundException {
        Account account = accountService.getAccountByUsername(username);
        GetAccountDetailedDTO accountDto = accountDTOConverter.toAccountDetailedDTO(account, timezone);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, eTag).body(accountDto);
    }


    @PutMapping("/{id}/user-data")
    public ResponseEntity<GetAccountPersonalDTO> updateAccountData(@RequestHeader("If-Match") String eTag, @PathVariable UUID id,
                                                                   @RequestBody UpdateAccountDataDTO updateAccountDataDTO)
            throws NotFoundException, OptLockException {
        GetAccountPersonalDTO updatedAccount =
                accountDTOConverter.toAccountPersonalDTO(accountService.updateAccountData(id, accountDTOConverter
                        .toAccount(updateAccountDataDTO), eTag));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @GetMapping("/participants")
    public ResponseEntity<List<GetAccountDTO>> getParticipants() throws NotFoundException {
        List<GetAccountDTO> participants = accountDTOConverter.accountDtoList(accountService.getParticipants());
        return ResponseEntity.status(HttpStatus.OK).body(participants);
    }

    @GetMapping("/administrators")
    public ResponseEntity<List<GetAccountDTO>> getAdministrators() throws NotFoundException {
        List<GetAccountDTO> administrators = accountDTOConverter.accountDtoList(accountService.getAdmins());
        return ResponseEntity.status(HttpStatus.OK).body(administrators);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<GetAccountDTO>> getManagers() throws NotFoundException {
        List<GetAccountDTO> managers = accountDTOConverter.accountDtoList(accountService.getManagers());
        return ResponseEntity.status(HttpStatus.OK).body(managers);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UpdateEmailDTO emailDTO) throws AccountNotFoundException {
        accountService.resetPasswordAndSendEmail(emailDTO.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/reset-password/token/{token}")
    public ResponseEntity<?> resetPasswordWithToken(@PathVariable String token, @RequestBody UpdatePasswordDTO password)
            throws TokenExpiredException, AccountNotFoundException, ThisPasswordAlreadyWasSetInHistory, TokenNotFoundException {
        accountService.resetPasswordWithToken(token, password.value());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UpdateEmailDTO emailDTO) throws AccountNotFoundException {
        accountService.changePasswordAndSendEmail(emailDTO.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-email/{id}")
    public ResponseEntity<?> changeEmail(@PathVariable UUID id, @RequestBody UpdateEmailDTO emailDTO) throws AccountNotFoundException {
        accountService.sendMailWhenEmailChange(id, emailDTO.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-email/token/{token}")
    public ResponseEntity<?> changeEmailWithToken(@PathVariable String token, @RequestBody UpdateEmailDTO email)
            throws TokenExpiredException, AccountNotFoundException, EmailAlreadyExistsException, TokenNotFoundException {
        accountService.changeEmailWithToken(token, email.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}