package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import jakarta.validation.constraints.Size;

public record GetTimeZoneDTO(
    @Size(min = 3, max = 32)
    String timeZone
){}

