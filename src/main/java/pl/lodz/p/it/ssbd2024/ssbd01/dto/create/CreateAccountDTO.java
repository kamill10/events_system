package pl.lodz.p.it.ssbd2024.ssbd01.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;

public record CreateAccountDTO(
        @Size(min = 3, max = 32)
        @NotNull String username,
        @Size(min = 8, max = 72)
        @NotNull String password,
        @Email
        @NotNull String email,
        @NotNull
        @NotNull Integer gender,
        @Size(min = 2, max = 32)
        String firstName,
        @Size(min = 2, max = 64)
        String lastName,
        LanguageEnum  language
) {
}
