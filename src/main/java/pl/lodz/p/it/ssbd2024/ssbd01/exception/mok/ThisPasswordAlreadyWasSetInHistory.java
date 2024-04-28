package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class ThisPasswordAlreadyWasSetInHistory extends BadRequestException {
    public ThisPasswordAlreadyWasSetInHistory(String message) {
        super(message);
    }
}
