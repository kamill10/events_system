package pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.get;

import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;

import java.util.List;
import java.util.UUID;

public record GetAccountPersonalDTO(

        UUID id,

        String username,

        String email,

        List<AccountRoleEnum> roles,

        Boolean active,

        Boolean verified,

        Boolean nonLocked,

        String firstName,

        String lastName,

        LanguageEnum language,

        Integer gender,

        String accountTheme,

        String accountTimeZone
) {
}
