package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.ActionTypeEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountTheme;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountTimeZone;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetAccountHistoryDetailedDTO(
        @NotNull
        UUID id,

        @Size(min = 3, max = 32)
        String username,

        @Email
        String email,

        @NotNull
        List<AccountRoleEnum> roles,

        @NotNull
        Boolean active,

        @NotNull
        Boolean verified,

        @NotNull
        Boolean nonLocked,

        @NotNull
        Integer failedLoginAttempts,

        String lastFailedLoginIp,

        String lastSuccessfulLoginIp,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastSuccessfulLogin,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastFailedLogin,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lockedUntil,

        @PositiveOrZero
        Integer gender,

        @NotNull
        String firstName,

        @NotNull
        String lastName,

        @NotNull
        LanguageEnum language,

        AccountTheme accountTheme,

        AccountTimeZone accountTimeZone,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,

        Account createdBy,

        Account updatedBy,

        @Enumerated(EnumType.STRING)
        ActionTypeEnum actionType

) {
}
