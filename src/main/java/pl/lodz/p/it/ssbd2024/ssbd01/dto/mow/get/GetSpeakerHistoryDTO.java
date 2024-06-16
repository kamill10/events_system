package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.ActionTypeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetSpeakerHistoryDTO(

        UUID id,

        String firstName,

        String lastName,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,

        String createdBy,

        String updatedBy,

        ActionTypeEnum actionType

) {
}
