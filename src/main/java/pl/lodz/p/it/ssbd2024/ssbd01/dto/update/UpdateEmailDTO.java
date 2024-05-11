package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Email;

public record UpdateEmailDTO(
        @Email
        String email
) {
}
