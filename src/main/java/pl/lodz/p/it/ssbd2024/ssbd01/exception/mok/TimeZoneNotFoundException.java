package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class TimeZoneNotFoundException extends NotFoundException {
    public TimeZoneNotFoundException(String message) {
        super(message);
    }
}
