package pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateAccountDataDTO(
        @Size(min = 2, max = 32, message = ExceptionMessages.INCORRECT_FIRST_NAME)
        String firstName,
        @Size(min = 2, max = 64, message = ExceptionMessages.INCORRECT_LAST_NAME)
        String lastName,
        @NotNull(message = ExceptionMessages.INCORRECT_GENDER)
        Integer gender,
        @Size(max = 32)
        String timeZone,
        @Size(max = 32)
        String theme
) {
}
