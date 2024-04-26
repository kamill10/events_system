package pl.lodz.p.it.ssbd2024.ssbd01.mok.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdatePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.UnprocessableEntityException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.PasswordTokenExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.ThisPasswordAlreadyWasSetInHistory;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converter.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AccountDTOConverter accountDTOConverter;


      @GetMapping
    public List<GetAccountDTO> getAllUsers() {
        List<GetAccountDTO> getAccountDTOS = accountDTOConverter.accountDtoList(accountService.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
    }

    @PostMapping
    public ResponseEntity<GetAccountDTO> createUser(@RequestBody CreateAccountDTO createAccountDTO) {
        GetAccountDTO getAccountDTO = accountDTOConverter.toAccountDto(accountService.addAccount(accountDTOConverter.toAccount(createAccountDTO)));
        return ResponseEntity.status(HttpStatus.CREATED).body(getAccountDTO);
    }

    @PostMapping("/{id}/add-role")
    public ResponseEntity<GetAccountDTO> addRoleToAccount(@PathVariable UUID id, @RequestParam AccountRoleEnum roleName)
            throws BadRequestException, UnprocessableEntityException, NotFoundException, ConflictException {
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
    public ResponseEntity<GetAccountDTO> getAccountByUsername(@PathVariable String username) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            if (userDetails.getUsername().equals(username)) {
                GetAccountDTO accountDto = accountDTOConverter.toAccountDto(accountService.getAccountByUsername(username));
                return ResponseEntity.status(HttpStatus.OK).body(accountDto);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PutMapping("/{id}/user-data")
    public ResponseEntity<GetAccountDTO> updateAccountData(@PathVariable UUID id, @RequestBody UpdateAccountDataDTO updateAccountDataDTO)
            throws NotFoundException {
        GetAccountDTO updatedAccount =
                accountDTOConverter.toAccountDto(accountService.updateAccountData(id, accountDTOConverter.toAccount(updateAccountDataDTO)));
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

    @PatchMapping("/{id}/email")
    public ResponseEntity<GetAccountDTO> updateAccountEmail(@PathVariable UUID id, @RequestBody UpdateEmailDTO email)
            throws AccountNotFoundException, EmailAlreadyExistsException {
        GetAccountDTO updatedAccount = accountDTOConverter
                .toAccountDto(accountService.updateAccountEmail(id, email.email()));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updateAccountPassword(@PathVariable UUID id, @RequestBody UpdatePasswordDTO password)
            throws AccountNotFoundException, ThisPasswordAlreadyWasSetInHistory {
        accountService.updatePassword(id, password.value());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UpdateEmailDTO email) {
        accountService.resetPassword(email.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reset-password/token/{token}")
    public ResponseEntity<?> resetPasswordWithToken(@PathVariable String token, @RequestBody UpdatePasswordDTO password)
            throws PasswordTokenExpiredException, AccountNotFoundException, ThisPasswordAlreadyWasSetInHistory {
        accountService.resetPasswordWithToken(token, password.value());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}