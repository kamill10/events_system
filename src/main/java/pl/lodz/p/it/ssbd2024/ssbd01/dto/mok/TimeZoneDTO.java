package pl.lodz.p.it.ssbd2024.ssbd01.dto.mok;

import jakarta.validation.constraints.Size;

public record TimeZoneDTO(
    @Size(min = 3, max = 32)
    String timeZone
){}

