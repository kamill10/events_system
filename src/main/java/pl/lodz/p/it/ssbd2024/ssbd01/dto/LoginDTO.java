package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull String username,
        @NotNull String password) {
}
