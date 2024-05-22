package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class AccountUnlockTokenNotFoundException extends NotFoundException {

    public AccountUnlockTokenNotFoundException(String message) {
        super(message);
    }

}
