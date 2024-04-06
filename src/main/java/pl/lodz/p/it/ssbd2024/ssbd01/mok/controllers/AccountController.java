package pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.converters.AccountToAccountDto;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    @Autowired
    UserService userService;
    @GetMapping
    public List<AccountDto> getAllUsers() {
        List<AccountDto> accountDtos = AccountToAccountDto.accountDtoList(userService.getAllUsers());
        return ResponseEntity.status(HttpStatus.OK).body(accountDtos).getBody();
    }
    @PostMapping
    public ResponseEntity<AccountDto> createUser( Account account) {
        AccountDto accountDto = AccountToAccountDto.toAccountDto( userService.addUser(account));
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }
    @PostMapping("/{id}/addRole")
    public ResponseEntity<AccountDto> addRoleToAccount(@PathVariable UUID id,
                                                       @RequestParam String roleName) {
        AccountDto updatedAccount =AccountToAccountDto
                .toAccountDto(userService.addRoleToAccount(id, roleName));
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }
}
