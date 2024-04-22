package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;

public class PasswordTokenExpiredException extends ConflictException {
    public PasswordTokenExpiredException(String message) {
        super(message);
    }
}
