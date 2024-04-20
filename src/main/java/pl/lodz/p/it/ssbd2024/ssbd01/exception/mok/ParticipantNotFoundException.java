package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class ParticipantNotFoundException extends NotFoundException {
    public ParticipantNotFoundException(String message) {
        super(message);
    }

}
