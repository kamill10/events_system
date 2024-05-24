package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountHistoryDTO(
        @Size(min = 3, max = 32)
        @NotNull String username
) {
}
