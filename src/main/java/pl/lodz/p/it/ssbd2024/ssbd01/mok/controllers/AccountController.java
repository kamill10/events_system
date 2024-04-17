package pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converters.AccountToAccountDto;
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
    private final  PasswordEncoder passwordEncoder;

    @GetMapping
    public List<GetAccountDTO> getAllUsers() {
        List<GetAccountDTO> getAccountDTOS = AccountToAccountDto.accountDtoList(accountService.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
    }

    @PostMapping
    public ResponseEntity<GetAccountDTO> createUser(@RequestBody CreateUserRequest request) {
        Account account = new Account(request.getUsername(),passwordEncoder.encode(request.getPassword()), request.getEmail()
                , request.getGender(), request.getFirstName(), request.getLastName());
        GetAccountDTO getAccountDTO = AccountToAccountDto.toAccountDto(accountService.addAccount(account));
        return ResponseEntity.status(HttpStatus.CREATED).body(getAccountDTO);
    }

    @PostMapping("/{id}/addRole")
    public ResponseEntity<GetAccountDTO> addRoleToAccount(@PathVariable UUID id,
                                                          @RequestParam String roleName) {
        GetAccountDTO updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.addRoleToAccount(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @DeleteMapping("/{id}/removeRole")
    public ResponseEntity<GetAccountDTO> removeRole(@PathVariable UUID id,
                                                    @RequestParam String roleName) {
        GetAccountDTO updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.removeRole(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }
    @PatchMapping("/{id}/setActive")
    public ResponseEntity<GetAccountDTO> setActive(@PathVariable UUID id) {
        GetAccountDTO updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.setAccountStatus(id, true));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }
    @PatchMapping("/{id}/setInactive")
    public ResponseEntity<GetAccountDTO> setInactive(@PathVariable UUID id) {
        GetAccountDTO updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.setAccountStatus(id, false));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }
}
