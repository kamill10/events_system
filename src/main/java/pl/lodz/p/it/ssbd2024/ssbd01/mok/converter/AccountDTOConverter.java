package pl.lodz.p.it.ssbd2024.ssbd01.mok.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountHistoryDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetAccountPersonalDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.update.UpdateAccountDataDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountHistory;
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
                account.getFirstName(),
                account.getLastName(),
                account.getRoles().stream().map(Role::getName).toList(),
                account.getActive(),
                account.getVerified(),
                account.getNonLocked());
    }

    public GetAccountPersonalDTO toAccountPersonalDTO(Account account) {
        String theme = account.getAccountTheme() != null ? account.getAccountTheme().getTheme() : null;
        String timeZone = account.getAccountTimeZone() != null ? account.getAccountTimeZone().getTimeZone().toZoneId().getId() : null;

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
                account.getGender(),
                theme,
                timeZone
        );
    }

    public GetAccountDetailedDTO toAccountDetailedDTO(Account account) {

        String theme = account.getAccountTheme() != null ? account.getAccountTheme().getTheme() : null;
        String timeZone = account.getAccountTimeZone() != null ? account.getAccountTimeZone().getTimeZone().toZoneId().getId() : null;

        return new GetAccountDetailedDTO(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getRoles().stream().map(Role::getName).toList(),
                account.getActive(),
                account.getVerified(),
                account.getNonLocked(),
                account.getLastSuccessfulLogin(),
                account.getLastFailedLogin(),
                account.getLockedUntil(),
                account.getGender(),
                account.getFirstName(),
                account.getLastName(),
                account.getLanguage(),
                theme,
                timeZone
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

    public List<GetAccountHistoryDetailedDTO> accountHistoryDtoList(List<AccountHistory> accountHistories) {
        return accountHistories.stream().map(this::toAccountHistoryDto).toList();
    }

    public GetAccountHistoryDetailedDTO toAccountHistoryDto(AccountHistory accountHistory) {

        String theme = accountHistory.getAccountTheme() != null ? accountHistory.getAccountTheme().getTheme() : null;
        String timeZone = accountHistory.getAccountTimeZone() != null ? accountHistory.getAccountTimeZone().getTimeZone().toZoneId().getId() : null;
        String createdBy = accountHistory.getCreatedBy() != null ? accountHistory.getCreatedBy().getUsername() : null;
        String updatedBy = accountHistory.getUpdatedBy() != null ? accountHistory.getUpdatedBy().getUsername() : null;

        return new GetAccountHistoryDetailedDTO(
                accountHistory.getId(),
                accountHistory.getUsername(),
                accountHistory.getEmail(),
                accountHistory.getRoles().stream().map(Role::getName).toList(),
                accountHistory.getActive(),
                accountHistory.getVerified(),
                accountHistory.getNonLocked(),
                accountHistory.getFailedLoginAttempts(),
                accountHistory.getLastFailedLoginIp(),
                accountHistory.getLastSuccessfulLoginIp(),
                accountHistory.getLastSuccessfulLogin(),
                accountHistory.getLastFailedLogin(),
                accountHistory.getLockedUntil(),
                accountHistory.getGender(),
                accountHistory.getFirstName(),
                accountHistory.getLastName(),
                accountHistory.getLanguage(),
                theme,
                timeZone,
                accountHistory.getCreatedAt(),
                accountHistory.getUpdatedAt(),
                createdBy,
                updatedBy,
                accountHistory.getActionType()
        );
    }


    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDto).toList();
    }

    public Page<GetAccountDTO> accountDTOPage(Page<Account> accountPage) {
        return accountPage.map(this::toAccountDto);
    }

    private String convertAndFormatDateTime(LocalDateTime dateTime, ZoneId targetZoneId, DateTimeFormatter formatter) {
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(targetZoneId);
        return zonedDateTime.format(formatter);
    }

}
