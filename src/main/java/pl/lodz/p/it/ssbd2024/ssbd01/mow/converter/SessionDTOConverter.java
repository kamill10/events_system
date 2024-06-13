package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionForListDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;

import java.util.List;


public class SessionDTOConverter {

    public static Session getSession(CreateSessionDTO createSessionDTO) {
        return new Session(
                createSessionDTO.name(),
                createSessionDTO.isActive(),
                createSessionDTO.description(),
                createSessionDTO.startDate(),
                createSessionDTO.endDate(),
                createSessionDTO.maxSeats()
        );
    }

    public static GetSessionDetailedDTO toGetDetailedSessionSession(Session session) {
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
                EventDTOConverter.getEventPlDTO(session.getEvent())
        );
    }

    public static GetSessionDetailedDTO toSessionPlDetailedDTO(Session session) {
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
                EventDTOConverter.getEventPlDTO(session.getEvent())
        );
    }

    public static GetSessionForListDTO getSessionForListDTO(Session session) {
        return new GetSessionForListDTO(
                session.getId(),
                session.getName(),
                session.getDescription(),
                session.getStartTime(),
                session.getEndTime(),
                session.getMaxSeats(),
                session.getIsActive(),
                SpeakerDTOConverter.convertToDTO(session.getSpeaker()),
                RoomDTOConverter.toRoomDetailedDto(session.getRoom())
        );
    }

    public static List<GetSessionForListDTO> getSessionsForListDTO(List<Session> session) {
        return session.stream().map(SessionDTOConverter::getSessionForListDTO).toList();
    }
}