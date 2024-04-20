package pl.lodz.p.it.ssbd2024.ssbd01.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.BadRequestException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.ConflictException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.NotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.UnprocessableEntityException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;

@RestControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler
    ResponseEntity<String> handleAccountNotFoundException(NotFoundException e) {
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




}
