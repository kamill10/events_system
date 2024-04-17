package pl.lodz.p.it.ssbd2024.ssbd01.mok.converters;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;

import java.util.List;

public class AccountToAccountDto {
    public static GetAccountDTO toAccountDto(Account account){
        return new GetAccountDTO(account.getId(), account.getUsername(), account.getEmail(),account.getGender(),
                account.getFirstName()
                , account.getLastName());
    }
    public static List<GetAccountDTO> accountDtoList(List<Account>accounts){
        return accounts.stream().map(AccountToAccountDto::toAccountDto).toList();
    }
}
