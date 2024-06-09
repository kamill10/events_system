package pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.get;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetAccountDetailedDTO(
        UUID id,

        String username,

        String email,

        List<AccountRoleEnum> roles,

        Boolean active,

        Boolean verified,

        Boolean nonLocked,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastSuccessfulLogin,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastFailedLogin,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lockedUntil,

        Integer gender,

        String firstName,

        String lastName,

        LanguageEnum language,

        String accountTheme,

        String accountTimeZone
) {}
