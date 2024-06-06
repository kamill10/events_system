package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class SessionNotActiveException extends BadRequestException {
    public SessionNotActiveException(String message) {
        super(message);
    }
}
