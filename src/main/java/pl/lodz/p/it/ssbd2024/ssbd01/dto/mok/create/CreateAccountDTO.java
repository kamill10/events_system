package pl.lodz.p.it.ssbd2024.ssbd01.dto.create;

import jakarta.validation.constraints.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record CreateAccountDTO(
        @Pattern(regexp = "^(?!system$)(?!anonymous$)[a-zA-Z0-9]{3,32}$", message = ExceptionMessages.INCORRECT_USERNAME)
        @NotNull(message = ExceptionMessages.INCORRECT_USERNAME)
        String username,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,72}$", message = ExceptionMessages.INCORRECT_PASSWORD)
        @NotNull(message = ExceptionMessages.INCORRECT_PASSWORD)
        String password,
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
    @Override
    public String toString() {
        return "CreateAccountDTO{"
                + "username='" + username + '\''
                + ", password='********'"
                + ", email='" + email + '\''
                + ", gender=" + gender
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", language=" + language
                + '}';
    }
}
