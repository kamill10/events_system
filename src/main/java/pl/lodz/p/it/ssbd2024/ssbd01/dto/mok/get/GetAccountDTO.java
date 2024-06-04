package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import java.util.List;
import java.util.UUID;

public record GetAccountDTO(

        UUID id,

        String username,

        String email,

        String firstName,

        String lastName,

        List<AccountRoleEnum> roles,

        Boolean active,

        Boolean verified,

        Boolean nonLocked
) {
}
