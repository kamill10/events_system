package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;


public class SessionDTOConverter {

    public static GetSessionDetailedDTO toSessionDetailedDTO(Session session) {
        return new GetSessionDetailedDTO(
                session.getId(),
                session.getName(),
                session.getDescription(),
                session.getStartTime(),
                session.getEndTime(),
                session.getMaxSeats(),
                session.getIsActive(),
                SpeakerDTOConverter.convertToDTO(session.getSpeaker()),
                RoomDTOConverter.toRoomDetailedDto(session.getRoom()),
                EventDTOConverter.getEventDTO(session.getEvent())
        );
    }
}
