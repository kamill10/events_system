package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.UnprocessableEntityException;

public class AccountRolesLimitExceedException extends UnprocessableEntityException {
    public AccountRolesLimitExceedException(String message) {
        super(message);
    }
}
