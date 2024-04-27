package pl.lodz.p.it.ssbd2024.ssbd01.exception.auth;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class AccountConfirmationTokenNotFoundException extends NotFoundException {
    public AccountConfirmationTokenNotFoundException(String message) {
        super(message);
    }
}
