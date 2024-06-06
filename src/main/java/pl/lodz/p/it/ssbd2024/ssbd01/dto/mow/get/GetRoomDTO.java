package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;


import java.util.UUID;

public record GetRoomDTO(
        UUID id,
        String name,
        Integer maxCapacity
) {
}
