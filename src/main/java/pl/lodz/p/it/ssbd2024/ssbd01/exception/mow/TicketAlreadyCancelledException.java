package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class TicketAlreadyCancelledException extends BadRequestException {
    public TicketAlreadyCancelledException(String message) {
        super(message);
    }
}
