package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import java.util.UUID;

public record GetRoomDetailedDTO(

        UUID id,

        String name,

        Integer maxCapacity,

        GetLocationDTO location
) {
}