package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import java.time.LocalDate;
import java.util.UUID;

public record GetEventDTO(
        UUID id,

        String name,

        String description,

        Boolean isNotCanceled,

        LocalDate startDate,

        LocalDate endDate
) {
}
