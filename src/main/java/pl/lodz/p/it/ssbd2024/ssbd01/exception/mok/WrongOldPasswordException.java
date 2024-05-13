package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class WrongOldPasswordException extends BadRequestException {
    public WrongOldPasswordException(String message) {
        super(message);
    }
}
