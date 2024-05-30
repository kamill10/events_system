package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateMyEmailDTO(
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,72}$", message = ExceptionMessages.INCORRECT_PASSWORD)
        String password,
        @Email(message = ExceptionMessages.INCORRECT_EMAIL)
        String newEmail
) {
}
