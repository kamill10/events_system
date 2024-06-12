package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSessionDTO (
        @NotNull
        UUID  eventId,
        @NotNull
        UUID  roomId,
        @NotNull
        UUID speakerId,

        @NotBlank(message = ExceptionMessages.INCORRECT_NAME)
        @Size(min = 3, max = 128, message = ExceptionMessages.INCORRECT_NAME)
        @NotNull
        String name,


        @NotBlank(message = ExceptionMessages.INCORRECT_DESCRIPTION)
        @Size(min = 3, max = 1024, message = ExceptionMessages.INCORRECT_DESCRIPTION)
        String description,
        @NotNull(message = ExceptionMessages.FIELD_REQUIRED)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime startDate,

        @NotNull(message = ExceptionMessages.FIELD_REQUIRED)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime endDate,
        @Positive
        Integer maxSeats,
        @NotNull
                Boolean isActive


){}