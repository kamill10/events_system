package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateMyEmailDTO(
        @Size(min = 8, max = 64, message = ExceptionMessages.INCORRECT_PASSWORD)
        String password,
        @Email(message = ExceptionMessages.INCORRECT_EMAIL)
        String newEmail
) {
        @Override
        public String toString() {
                return "UpdateMyEmailDTO{" +
                        "password='********'" + // Protect sensitive information
                        ", newEmail='" + newEmail + '\'' +
                        '}';
        }
}
