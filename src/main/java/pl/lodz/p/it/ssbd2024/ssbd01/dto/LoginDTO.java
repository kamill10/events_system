package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @Size(min = 3, max = 32)
        @NotNull String username,
        @Size(min = 8, max = 72)
        @NotNull String password) {

    @Override
    public String toString() {
        return "LoginDTO{"
                + "username='" + username + '\''
                + ", password='********'"
                + '}';
    }
}
