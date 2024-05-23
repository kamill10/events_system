package pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception;

public abstract class UnauthorizedOperationException extends Exception {
    protected UnauthorizedOperationException(String message) {
        super(message);
    }
}
