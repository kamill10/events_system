package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record GetLocationDTO(

        UUID id,

        @NotBlank
        @Size(min = 3, max = 32)
        String name,

        @NotBlank
        String city,

        @NotBlank
        String country,

        @NotBlank
        String street,

        @NotBlank
        String buildingNumber,

        @Pattern(regexp = "\\d{2}-\\d{3}")
        String postalCode
) {
}
