package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.AbstractException;

public class AccountNotFoundException extends AbstractException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
