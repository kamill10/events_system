package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetTicketDetailedDTO(
        UUID id,

        GetSessionDetailedDTO session,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime reservationTime,

        Boolean isNotCancelled

) {
}
