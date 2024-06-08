package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateRoomDTO(
        @NotNull
        String name,

        @NotNull
        UUID locationId,

        @Positive
        Integer maxCapacity
) {
}
