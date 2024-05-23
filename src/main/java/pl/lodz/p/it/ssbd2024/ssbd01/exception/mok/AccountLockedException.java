package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.UnauthorizedOperationException;

public class AccountLockedException extends UnauthorizedOperationException {

        public AccountLockedException(String message) {
            super(message);
        }

}
