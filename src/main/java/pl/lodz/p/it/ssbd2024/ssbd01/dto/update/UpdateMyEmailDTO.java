package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateMyEmailDTO(
        @Size(min = 8, max = 64)
        String password,
        @Email
        String newEmail
) {
}
