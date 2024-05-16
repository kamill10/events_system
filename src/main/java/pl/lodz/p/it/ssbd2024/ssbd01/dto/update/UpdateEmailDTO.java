package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateEmailDTO(
        @Email(message = ExceptionMessages.INCORRECT_EMAIL)
        String email
) {
}
