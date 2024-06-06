package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
