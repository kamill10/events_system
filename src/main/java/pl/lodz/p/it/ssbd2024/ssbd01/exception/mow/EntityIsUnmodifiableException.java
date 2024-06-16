package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ForbiddenException;

public class EntityIsUnmodifiableException extends ForbiddenException {
    public EntityIsUnmodifiableException(String message) {
        super(message);
    }
}
