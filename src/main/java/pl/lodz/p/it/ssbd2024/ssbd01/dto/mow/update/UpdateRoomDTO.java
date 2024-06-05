package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update;

import jakarta.validation.constraints.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateRoomDTO(
        @NotBlank(message = ExceptionMessages.INCORRECT_NAME)
        @Size(min = 3, max = 32,message = ExceptionMessages.INCORRECT_NAME)
        String name,

        @NotNull(message = ExceptionMessages.INCORRECT_MAX_CAPACITY)
        @Min(value = 1,message = ExceptionMessages.INCORRECT_MAX_CAPACITY)
        @Max(value = 1000,message = ExceptionMessages.INCORRECT_MAX_CAPACITY)
        Integer maxCapacity
) {
}