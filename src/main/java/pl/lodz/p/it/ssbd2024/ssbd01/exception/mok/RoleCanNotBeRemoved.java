package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;

public class RoleCanNotBeRemoved extends BadRequestException {

        public RoleCanNotBeRemoved(String message) {
            super(message);
        }
}
