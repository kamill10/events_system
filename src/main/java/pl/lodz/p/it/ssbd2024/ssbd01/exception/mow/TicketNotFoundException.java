package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class TicketNotFoundException extends NotFoundException {

    public TicketNotFoundException(String message) {
        super(message);
    }
}