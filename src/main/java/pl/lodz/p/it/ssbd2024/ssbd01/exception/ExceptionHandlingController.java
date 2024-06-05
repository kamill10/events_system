package pl.lodz.p.it.ssbd2024.ssbd01.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.AnnotationException;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.MappingException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        var violation = e.getConstraintViolations().iterator().next();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(violation.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUnexpectedRollbackException(org.springframework.transaction.UnexpectedRollbackException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction failed");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleHibernateConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        var array = Objects.requireNonNull(e.getConstraintName()).split("_");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(array[array.length - 2] + " exists.");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleSignatureException(SignatureException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedOperationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var error = ex.getBindingResult().getFieldErrors().getFirst();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getDefaultMessage());
    }


    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<String> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

//    @ExceptionHandler(MappingException.class)
//    public ResponseEntity<String> handleMappingException(MappingException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMessages.MAPPING_ERROR);
//    }
//
//    @ExceptionHandler(AnnotationException.class)
//    public ResponseEntity<String> handleAnnotationException(AnnotationException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMessages.ANNOTATION_EXCEPTION);
//    }
//
//    @ExceptionHandler(JDBCConnectionException.class)
//    public ResponseEntity<String> handleJDBCConnectionException(JDBCConnectionException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMessages.JDBC_CONNECTION_ERROR);
//    }
//
//    @ExceptionHandler(LazyInitializationException.class)
//    public ResponseEntity<String> handleLazyInitializationException(LazyInitializationException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMessages.LAZY_INITIALIZATION_ERROR);
//    }
//
//    @ExceptionHandler(HibernateException.class)
//    public ResponseEntity<String> handleHibernateException(HibernateException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMessages.HIBERNATE_EXCEPTION);
//    }
}
