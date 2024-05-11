package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAccountDTO(
        @Email
        String email,
        @NotNull
        Integer gender,
        @Size(min = 8, max = 72)
        String password
) {
}
