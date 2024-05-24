package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateEmailDTO(
        @Email(message = ExceptionMessages.INCORRECT_EMAIL)
        @NotBlank(message = ExceptionMessages.INCORRECT_EMAIL)
        String email
) {
}
