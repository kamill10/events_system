package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.PreconditionFailedException;

public class OptLockException extends PreconditionFailedException {

    public OptLockException(String message) {
        super(message);
    }

}
