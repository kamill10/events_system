package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class ManagersNotFoundException extends NotFoundException {
    public ManagersNotFoundException(String message) {
        super(message);
    }
}
