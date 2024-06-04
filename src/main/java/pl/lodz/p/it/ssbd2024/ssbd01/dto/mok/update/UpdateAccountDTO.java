package pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record UpdateAccountDTO(
        @Email(message = ExceptionMessages.INCORRECT_EMAIL)
        String email,
        @NotNull(message = ExceptionMessages.INCORRECT_GENDER)
        Integer gender,
        @Size(min = 8, max = 72, message = ExceptionMessages.INCORRECT_PASSWORD)
        String password
) {
    @Override
    public String toString() {
        return "UpdateAccountDTO{"
                + "email='" + email + '\''
                + ", gender=" + gender
                + ", password='********'"
                + '}';
    }
}
