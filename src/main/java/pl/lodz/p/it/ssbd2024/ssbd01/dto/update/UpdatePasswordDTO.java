package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdatePasswordDTO(

        @Size(min = 8, max = 72, message = ExceptionMessages.INCORRECT_PASSWORD)
        String value
) {
}
