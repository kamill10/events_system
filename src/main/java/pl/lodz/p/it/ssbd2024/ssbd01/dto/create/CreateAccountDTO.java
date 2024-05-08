package pl.lodz.p.it.ssbd2024.ssbd01.dto.create;

import jakarta.validation.constraints.NotNull;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;

public record CreateAccountDTO(
        @NotNull String username,
        @NotNull String password,
        @NotNull String email,
        @NotNull Integer gender,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull LanguageEnum language
) {
}
