package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateLocationDTO(
        @NotBlank
        @Size(min = 3, max = 32)
        String name,

        @NotBlank
        String street,

        @NotBlank
        String buildingNumber,

        @NotBlank
        @Pattern(regexp = "\\d{2}-\\d{3}")
        String postalCode,

        @NotBlank
        String city,

        @NotBlank
        String country
) {
}
