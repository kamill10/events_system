package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update;

import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateSpeakerDTO(
    @Size(min = 2, max = 32, message = ExceptionMessages.INCORRECT_FIRST_NAME)
    String firstName,
    @Size(min = 2, max = 64, message = ExceptionMessages.INCORRECT_LAST_NAME)
    String lastName) {
}
