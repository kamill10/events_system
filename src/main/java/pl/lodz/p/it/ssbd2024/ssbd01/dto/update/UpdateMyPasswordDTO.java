package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

import jakarta.validation.constraints.Size;

public record UpdateMyPasswordDTO(

        @Size(min = 8, max = 64)
        String oldPassword,
        @Size(min = 8, max = 64)
        String newPassword

) {}
