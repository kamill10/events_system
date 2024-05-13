package pl.lodz.p.it.ssbd2024.ssbd01.exception.mok;

import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;

public class TokenNotFoundException extends NotFoundException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
