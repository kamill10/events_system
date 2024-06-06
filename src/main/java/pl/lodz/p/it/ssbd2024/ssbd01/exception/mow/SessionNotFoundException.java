package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class SessionNotFoundException extends NotFoundException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
