package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class EventStartDateInPast extends BadRequestException {
    public EventStartDateInPast(String message) {
        super(message);
    }
}
