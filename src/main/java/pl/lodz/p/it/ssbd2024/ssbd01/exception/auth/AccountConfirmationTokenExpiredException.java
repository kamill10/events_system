package pl.lodz.p.it.ssbd2024.ssbd01.exception.auth;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;

public class AccountConfirmationTokenExpiredException extends ConflictException {
    public AccountConfirmationTokenExpiredException(String message) {
        super(message);
    }
}
