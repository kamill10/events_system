package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.AbstractException;

public class AccountRolesLimitExceedException extends AbstractException {
    public AccountRolesLimitExceedException(String message) {
        super(message);
    }
}
