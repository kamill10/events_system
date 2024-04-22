package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(

        @NotNull
        @Size(min = 8, max = 64)
        String value
) {
}
