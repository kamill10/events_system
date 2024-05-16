package pl.lodz.p.it.ssbd2024.ssbd01.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.UnprocessableEntityException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler
    ResponseEntity<String> handleEntityNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler
    ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler
    ResponseEntity<String> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler
    ResponseEntity<String> handleUnprocessableEntityException(UnprocessableEntityException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    @ExceptionHandler
    ResponseEntity<String> handleLockedException(LockedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler
    ResponseEntity<String> handleOptLockException(OptLockException e) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        var violations = e.getConstraintViolations();
        HashMap<String, String> errors = new HashMap<>();
        violations.forEach(violation ->
                errors.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUnexpectedRollbackException(org.springframework.transaction.UnexpectedRollbackException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction failed");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleHibernateConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        ArrayList<String> causes = new ArrayList<>();
        Collections.addAll(causes,e.getCause().getMessage().split("Detail: ")[1].split("\\(|\\)"));
        causes.remove(0);
        causes.remove(1);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new HashMap<>() {{
            put(causes.get(1),causes.get(0) + StringUtils.chop(causes.get(2)));
            }
        });
    }

}
