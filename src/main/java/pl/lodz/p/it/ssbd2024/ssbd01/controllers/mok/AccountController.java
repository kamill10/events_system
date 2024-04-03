package pl.lodz.p.it.ssbd2024.ssbd01.controllers.mok;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.converters.mok.AccountToAccountDto;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.AccountDto;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.services.mok.UserService;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    UserService userService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getAllUsers() {
        return AccountToAccountDto.accountDtoList(userService.getAllUsers());
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createUser( Account account) {
        return AccountToAccountDto.toAccountDto( userService.addUser(account));
    }
}
