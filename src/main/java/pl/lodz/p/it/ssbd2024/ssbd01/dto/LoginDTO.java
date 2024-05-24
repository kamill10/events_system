package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record LoginDTO(
        @Pattern(regexp = "^(?!anonymous$)[a-zA-Z0-9]{3,32}$", message = ExceptionMessages.INCORRECT_USERNAME)
        @Size(min = 3, max = 32,message = ExceptionMessages.INCORRECT_USERNAME)
        @NotNull String username,

        @Size(min = 8, max = 72, message = ExceptionMessages.INCORRECT_PASSWORD)
        @NotNull String password) {

    @Override
    public String toString() {
        return "LoginDTO{"
                + "username='" + username + '\''
                + ", password='********'"
                + '}';
    }
}
