package pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception;

public abstract class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
