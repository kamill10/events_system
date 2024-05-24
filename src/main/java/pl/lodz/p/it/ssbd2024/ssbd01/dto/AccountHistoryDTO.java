package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record AccountHistoryDTO(
        @Pattern(regexp = "^(?!anonymous$)[a-zA-Z0-9]{3,32}$", message = ExceptionMessages.INCORRECT_USERNAME)
        @NotNull(message = ExceptionMessages.INCORRECT_USERNAME)
        @NotNull String username
) {
}
