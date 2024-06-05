package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record GetEventDTO(
        UUID id,

        String name,

        String description,

        Boolean isNotCanceled,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
}
