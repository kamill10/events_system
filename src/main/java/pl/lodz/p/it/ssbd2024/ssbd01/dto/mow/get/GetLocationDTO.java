package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import java.util.UUID;

public record GetLocationDTO(
        UUID id,
        String name,
        String city,
        String country,
        String street,
        String buildingNumber,
        String postalCode
) {
}
