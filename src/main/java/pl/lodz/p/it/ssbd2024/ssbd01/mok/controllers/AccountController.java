package pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converters.AccountDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.request.CreateUserRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AccountDTOConverter AccountDTOConverter;

    @GetMapping
    public List<GetAccountDTO> getAllUsers() {
        List<GetAccountDTO> getAccountDTOS = AccountDTOConverter.accountDtoList(accountService.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
    }

    @PostMapping
    public ResponseEntity<GetAccountDTO> createUser(@RequestBody CreateUserRequest request) {
        Account account = new Account(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail()
                , request.getGender(), request.getFirstName(), request.getLastName());
        GetAccountDTO getAccountDTO = AccountDTOConverter.toAccountDto(accountService.addAccount(account));
        return ResponseEntity.status(HttpStatus.CREATED).body(getAccountDTO);
    }

    @PostMapping("/{id}/addRole")
    public ResponseEntity<GetAccountDTO> addRoleToAccount(@PathVariable UUID id,
                                                          @RequestParam String roleName) {
        GetAccountDTO updatedAccount = AccountDTOConverter
                .toAccountDto(accountService.addRoleToAccount(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @DeleteMapping("/{id}/removeRole")
    public ResponseEntity<GetAccountDTO> removeRole(@PathVariable UUID id,
                                                    @RequestParam String roleName) {
        GetAccountDTO updatedAccount = AccountDTOConverter
                .toAccountDto(accountService.removeRole(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/setActive")
    public ResponseEntity<GetAccountDTO> setActive(@PathVariable UUID id) {
        GetAccountDTO updatedAccount = AccountDTOConverter
                .toAccountDto(accountService.setAccountStatus(id, true));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/setInactive")
    public ResponseEntity<GetAccountDTO> setInactive(@PathVariable UUID id) {
        GetAccountDTO updatedAccount = AccountDTOConverter
                .toAccountDto(accountService.setAccountStatus(id, false));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<GetAccountDTO> getAccountByUsername(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            if (userDetails.getUsername().equals(username)) {
                GetAccountDTO accountDto = AccountDTOConverter.toAccountDto(accountService.getAccountByUsername(username));
                return ResponseEntity.status(HttpStatus.OK).body(accountDto);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PutMapping("/userData/{id}")
    public ResponseEntity<GetAccountDTO> updateAccountUserData(@PathVariable UUID id, @RequestBody Account account) {
        GetAccountDTO updatedAccount = AccountDTOConverter
                .toAccountDto(accountService.updateAccountUserData(id, account));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }
    @GetMapping("/participants")
    public ResponseEntity<List<GetAccountDTO>> getParticipants() {
        List<GetAccountDTO> participants = AccountDTOConverter.
                accountDtoList(accountService.
                        getParticipants());
        return ResponseEntity.status(HttpStatus.OK).body(participants);
    }
    @GetMapping("/administrators")
    public ResponseEntity<List<GetAccountDTO>> getAdministrators() {
        List<GetAccountDTO> admiministrators = AccountDTOConverter.
                accountDtoList(accountService.getAdmins());
        return ResponseEntity.status(HttpStatus.OK).body(admiministrators);
    }
    @GetMapping("/managers")
    public ResponseEntity<List<GetAccountDTO>> getManagers() {
        List<GetAccountDTO> managers = AccountDTOConverter.
                accountDtoList(accountService.getMenagers() );
        return ResponseEntity.status(HttpStatus.OK).body(managers);
    }



}