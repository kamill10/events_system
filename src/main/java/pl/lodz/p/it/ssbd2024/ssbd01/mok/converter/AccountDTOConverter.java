package pl.lodz.p.it.ssbd2024.ssbd01.mok.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountPersonalDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class AccountDTOConverter {
    private final PasswordEncoder passwordEncoder;

    public GetAccountDTO toAccountDto(Account account) {
        return new GetAccountDTO(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getRoles().stream().map(Role::getName).toList(),
                account.getActive(),
                account.getVerified(),
                account.getNonLocked());
    }

    public GetAccountPersonalDTO toAccountPersonalDTO(Account account) {
        return new GetAccountPersonalDTO(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getRoles().stream().map(Role::getName).toList(),
                account.getActive(),
                account.getVerified(),
                account.getNonLocked(),
                account.getFirstName(),
                account.getLastName(),
                account.getLanguage(),
                account.getGender());
    }

    public GetAccountDetailedDTO toAccountDetailedDTO(Account account, String timezone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId targetZoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.systemDefault();

        String lastSuccessfulLogin =
                account.getLastSuccessfulLogin() != null ? convertAndFormatDateTime(account.getLastSuccessfulLogin(), targetZoneId, formatter) : null;
        String lastFailedLogin =
                account.getLastFailedLogin() != null ? convertAndFormatDateTime(account.getLastFailedLogin(), targetZoneId, formatter) : null;
        String lockedUntil = account.getLockedUntil() != null ? convertAndFormatDateTime(account.getLockedUntil(), targetZoneId, formatter) : null;

        return new GetAccountDetailedDTO(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getRoles().stream().map(Role::getName).toList(),
                account.getActive(),
                account.getVerified(),
                account.getNonLocked(),
                lastSuccessfulLogin,
                lastFailedLogin,
                lockedUntil,
                account.getGender(),
                account.getFirstName(),
                account.getLastName(),
                account.getLanguage()
        );
    }

    public Account toAccount(CreateAccountDTO createAccountDTO) {
        return new Account(
                createAccountDTO.username(),
                passwordEncoder.encode(createAccountDTO.password()),
                createAccountDTO.email(),
                createAccountDTO.gender(),
                createAccountDTO.firstName(),
                createAccountDTO.lastName(),
                createAccountDTO.language());
    }

    public Account toAccount(UpdateAccountDataDTO updateAccountDataDTO) {
        return new Account(
                updateAccountDataDTO.firstName(),
                updateAccountDataDTO.lastName(),
                updateAccountDataDTO.gender());
    }

    public UpdateAccountDataDTO toUpdateAccountDataDTO(Account account) {
        return new UpdateAccountDataDTO(
                account.getFirstName(),
                account.getLastName(),
                account.getGender());
    }


    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDto).toList();
    }

    private String convertAndFormatDateTime(LocalDateTime dateTime, ZoneId targetZoneId, DateTimeFormatter formatter) {
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(targetZoneId);
        return zonedDateTime.format(formatter);
    }

}
