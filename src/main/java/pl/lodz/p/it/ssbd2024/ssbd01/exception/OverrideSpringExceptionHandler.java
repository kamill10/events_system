package pl.lodz.p.it.ssbd2024.ssbd01.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

@RestControllerAdvice
public class OverrideSpringExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return switch (statusCode) {
            case HttpStatus.NOT_FOUND -> new ResponseEntity(ExceptionMessages.RESOURCE_NOT_FOUND, headers, statusCode);
            case HttpStatus.BAD_REQUEST -> new ResponseEntity(ExceptionMessages.BAD_REQUEST, headers, statusCode);
            case HttpStatus.UNPROCESSABLE_ENTITY -> new ResponseEntity(ExceptionMessages.UNPROCESSABLE_ENTITY, headers, statusCode);
            case HttpStatus.FORBIDDEN -> new ResponseEntity(ExceptionMessages.FORBIDDEN, headers, statusCode);
            case HttpStatus.PRECONDITION_FAILED -> new ResponseEntity(ExceptionMessages.PRECONDITION_FAILED, headers, statusCode);
            default -> new ResponseEntity(ExceptionMessages.SOMETHING_WENT_WRONG, headers, statusCode);
        };
    }
}
