package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;


public class EventDTOConverter {

    public static GetEventDTO getEventDTO(Event event) {
        return new GetEventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getIsNotCanceled(),
                event.getStartDate(),
                event.getEndDate()
        );
    }
}
