package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetSessionForListDTO(
        UUID id,
        String name,

        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endTime,

        Integer maxSeats,

        Boolean isActive,

        GetSpeakerDTO speaker,

        GetRoomDetailedDTO room) {
}
