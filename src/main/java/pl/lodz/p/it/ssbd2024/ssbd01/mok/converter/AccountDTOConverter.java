package pl.lodz.p.it.ssbd2024.ssbd01.mok.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDTOConverter {
    private final PasswordEncoder passwordEncoder;

    public GetAccountDTO toAccountDto(Account account) {
        return new GetAccountDTO(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getGender(),
                account.getFirstName(),
                account.getLastName());
    }

    public Account toAccount(CreateAccountDTO createAccountDTO) {
        return new Account(
                createAccountDTO.username(),
                passwordEncoder.encode(createAccountDTO.password()),
                createAccountDTO.email(),
                createAccountDTO.gender(),
                createAccountDTO.firstName(),
                createAccountDTO.lastName());
    }

    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDto).toList();
    }
}
