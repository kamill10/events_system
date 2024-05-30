package pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception;

public abstract class UnprocessableEntityException extends AppException {
    protected UnprocessableEntityException(String message) {
        super(message);
    }
}
