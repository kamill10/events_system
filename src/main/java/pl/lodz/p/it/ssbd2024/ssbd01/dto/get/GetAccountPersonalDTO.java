package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;

import java.util.List;
import java.util.UUID;

public record GetAccountPersonalDTO(
        @NotNull
        UUID id,
        @Size(min = 3, max = 32)
        String username,
        @Email
        String email,
        @NotNull
        List<Role> roles,
        @NotNull
        Boolean active,
        @NotNull
        Boolean verified,
        @NotNull
        Boolean nonLocked,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @NotNull
        LanguageEnum language,
        @PositiveOrZero
        Integer gender
) {
}
