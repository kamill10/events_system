package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class LocationAlreadyDeletedException extends BadRequestException {
    public LocationAlreadyDeletedException(String message) {
        super(message);
    }
}
