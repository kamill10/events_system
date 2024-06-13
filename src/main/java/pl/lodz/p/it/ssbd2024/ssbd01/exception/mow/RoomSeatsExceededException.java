package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;

public class RoomSeatsExceededException extends ConflictException {
    public RoomSeatsExceededException(String message) {
        super(message);
    }
}
