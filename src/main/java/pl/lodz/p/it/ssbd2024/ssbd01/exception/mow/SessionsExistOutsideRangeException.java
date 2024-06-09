package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class SessionsExistOutsideRangeException extends BadRequestException {
    public SessionsExistOutsideRangeException(String message) {
        super(message);
    }
}
