package pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception;

public abstract class NotFoundException extends Exception {
    protected NotFoundException(String message) {
        super(message);
    }
}