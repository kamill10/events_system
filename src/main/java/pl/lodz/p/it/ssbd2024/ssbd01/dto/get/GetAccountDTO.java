package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;

import java.util.List;
import java.util.UUID;

public record GetAccountDTO(
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
        Boolean nonLocked
){}
