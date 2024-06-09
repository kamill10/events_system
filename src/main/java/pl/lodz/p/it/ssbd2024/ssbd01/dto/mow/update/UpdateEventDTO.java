package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateEventDTO(
        @NotBlank(message = ExceptionMessages.INCORRECT_NAME)
        @Size(min = 3, max = 128, message = ExceptionMessages.INCORRECT_NAME)
        String name,

        @NotBlank(message = ExceptionMessages.INCORRECT_DESCRIPTION)
        @Size(min = 3, max = 1024, message = ExceptionMessages.INCORRECT_DESCRIPTION)
        String description,

        @NotNull(message = ExceptionMessages.FIELD_REQUIRED)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime startDate,

        @NotNull(message = ExceptionMessages.FIELD_REQUIRED)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime endDate
) {
}
