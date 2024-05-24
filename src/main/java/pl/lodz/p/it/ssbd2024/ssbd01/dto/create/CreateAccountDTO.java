package pl.lodz.p.it.ssbd2024.ssbd01.dto.create;

import jakarta.validation.constraints.*;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record CreateAccountDTO(
        @Pattern(regexp = "^(?!anonymous$)[a-zA-Z0-9]{3,32}$", message = ExceptionMessages.INCORRECT_USERNAME)
        @NotNull(message = ExceptionMessages.INCORRECT_USERNAME)
        String username,
        @Size(min = 8, max = 72, message = ExceptionMessages.INCORRECT_PASSWORD)
        @NotNull String password,
        @Email(message = ExceptionMessages.INCORRECT_EMAIL)
        @NotBlank(message = ExceptionMessages.INCORRECT_EMAIL)
        String email,
        @NotNull(message = ExceptionMessages.INCORRECT_GENDER)
        Integer gender,
        @Size(min = 2, max = 32, message = ExceptionMessages.INCORRECT_FIRST_NAME)
        String firstName,
        @Size(min = 2, max = 64, message = ExceptionMessages.INCORRECT_LAST_NAME)
        String lastName,

        LanguageEnum language
) {
}
