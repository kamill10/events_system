package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAccountDataDTO(
        @Size(min = 2, max = 32)
        String firstName,
        @Size(min = 2, max = 64)
        String lastName,
        @NotNull
        Integer gender
) {
}
