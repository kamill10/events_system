package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;

public class ThisPasswordAlreadyWasSetInHistory extends ConflictException {
    public ThisPasswordAlreadyWasSetInHistory(String message) {
        super(message);
    }
}
