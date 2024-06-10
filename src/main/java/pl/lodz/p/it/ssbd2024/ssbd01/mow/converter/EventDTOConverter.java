package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import org.springframework.data.domain.Page;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;

import java.util.ArrayList;


public class EventDTOConverter {

    public static GetEventDTO getEventPlDTO(Event event) {
        return new GetEventDTO(
                event.getId(),
                event.getName(),
                event.getDescriptionPL(),
                event.getIsNotCanceled(),
                event.getStartDate(),
                event.getEndDate()
        );
    }

    public static GetEventDTO getEventEnDTO(Event event) {
        return new GetEventDTO(
                event.getId(),
                event.getName(),
                event.getDescriptionEN(),
                event.getIsNotCanceled(),
                event.getStartDate(),
                event.getEndDate()
        );
    }

    public static Event getEvent(CreateEventDTO createEventDTO) {
        return new Event(
                createEventDTO.name(),
                createEventDTO.description(),
                new ArrayList<>(),
                createEventDTO.startDate(),
                createEventDTO.endDate()
        );
    }

    public static Event getEvent(UpdateEventDTO updateEventDTO) {
        return new Event(
                updateEventDTO.name(),
                updateEventDTO.description(),
                new ArrayList<>(),
                updateEventDTO.startDate(),
                updateEventDTO.endDate()
        );
    }

    public static Page<GetEventDTO> eventPlDTOPage(Page<Event> eventPage) {
        return eventPage.map(EventDTOConverter::getEventPlDTO);
    }

    public static Page<GetEventDTO> eventEnDTOPage(Page<Event> eventPage) {
        return eventPage.map(EventDTOConverter::getEventEnDTO);
    }
}