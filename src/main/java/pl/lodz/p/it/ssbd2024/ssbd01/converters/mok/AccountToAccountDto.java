package pl.lodz.p.it.ssbd2024.ssbd01.converters.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.AccountDto;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;

import java.util.List;

public class AccountToAccountDto {
    public static  AccountDto toAccountDto(Account account){
        return new AccountDto(account.getId(), account.getUsername(), account.getEmail(),account.getGender(),
                account.getFirstName()
                , account.getLastName());
    }
    public static List<AccountDto> accountDtoList(List<Account>accounts){
        return accounts.stream().map(AccountToAccountDto::toAccountDto).toList();
    }
}
