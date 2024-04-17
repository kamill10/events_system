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
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converters.AccountToAccountDto;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.dto.AccountDto;
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

    @GetMapping
    public List<AccountDto> getAllUsers() {
        List<AccountDto> accountDtos = AccountToAccountDto.accountDtoList(accountService.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(accountDtos).getBody();
    }

    @PostMapping
    public ResponseEntity<AccountDto> createUser(@RequestBody CreateUserRequest request) {
        Account account = new Account(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail()
                , request.getGender(), request.getFirstName(), request.getLastName());
        AccountDto accountDto = AccountToAccountDto.toAccountDto(accountService.addAccount(account));
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    @PostMapping("/{id}/addRole")
    public ResponseEntity<AccountDto> addRoleToAccount(@PathVariable UUID id,
                                                       @RequestParam String roleName) {
        AccountDto updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.addRoleToAccount(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @DeleteMapping("/{id}/removeRole")
    public ResponseEntity<AccountDto> removeRole(@PathVariable UUID id,
                                                 @RequestParam String roleName) {
        AccountDto updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.removeRole(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/setActive")
    public ResponseEntity<AccountDto> setActive(@PathVariable UUID id) {
        AccountDto updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.setAccountStatus(id, true));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @PatchMapping("/{id}/setInactive")
    public ResponseEntity<AccountDto> setInactive(@PathVariable UUID id) {
        AccountDto updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.setAccountStatus(id, false));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AccountDto> getAccountByUsername(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails.getUsername().equals(username)) {
                AccountDto accountDto = AccountToAccountDto.toAccountDto(accountService.getAccountByUsername(username));
                return ResponseEntity.status(HttpStatus.OK).body(accountDto);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PutMapping("/userData/{id}")
    public ResponseEntity<AccountDto> updateAccountUserData(@PathVariable UUID id, @RequestBody Account account) {
        AccountDto updatedAccount = AccountToAccountDto
                .toAccountDto(accountService.updateAccountUserData(id, account));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

}