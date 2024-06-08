package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDate;

public record CreateEventDTO(

        @NotBlank(message = ExceptionMessages.INCORRECT_NAME)
        @Size(min = 3, max = 128, message = ExceptionMessages.INCORRECT_NAME)
        String name,

        @NotBlank(message = ExceptionMessages.INCORRECT_DESCRIPTION)
        @Size(min = 3, max = 1024, message = ExceptionMessages.INCORRECT_DESCRIPTION)
        String description,

        @NotNull(message = ExceptionMessages.FIELD_REQUIRED)
        LocalDate startDate,

        @NotNull(message = ExceptionMessages.FIELD_REQUIRED)
        LocalDate endDate
) {
}
