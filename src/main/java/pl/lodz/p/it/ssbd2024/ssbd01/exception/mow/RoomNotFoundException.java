package pl.lodz.p.it.ssbd2024.ssbd01.exception.mow;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.RoomRepository;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
