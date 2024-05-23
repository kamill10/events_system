package pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception;

public abstract class PreconditionFailedException extends Exception {

    public PreconditionFailedException(String message) {
        super(message);
    }

}
