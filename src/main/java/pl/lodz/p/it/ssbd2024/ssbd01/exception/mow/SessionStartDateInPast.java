package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class SessionStartDateInPast extends BadRequestException {
    public SessionStartDateInPast(String message) {
        super(message);
    }
}
