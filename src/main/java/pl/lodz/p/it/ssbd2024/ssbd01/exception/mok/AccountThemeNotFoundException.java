package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class AccountThemeNotFoundException extends NotFoundException {

    public AccountThemeNotFoundException(String message) {
        super(message);
    }
}
