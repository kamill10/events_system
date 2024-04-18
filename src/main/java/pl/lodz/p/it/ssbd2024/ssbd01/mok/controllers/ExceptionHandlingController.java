package pl.lodz.p.it.ssbd2024.ssbd01.mok.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(AccountNotFoundException.class)
    ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AdminNotFoundException.class)
    ResponseEntity<String> handleAdminNotFoundException(AdminNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    ResponseEntity<String> handleParticipantNotFoundException(ParticipantNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    ResponseEntity<String> handleRoleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(WrongRoleToAccountException.class)
    ResponseEntity<String> handleWrongRoleToAccountException(WrongRoleToAccountException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(RoleAlreadyAssignedException.class)
    ResponseEntity<String> handleRoleAlreadyAssignedException(RoleAlreadyAssignedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(AccountRolesLimitExceedException.class)
    ResponseEntity<String> handleAccountRolesLimitException(AccountRolesLimitExceedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(RoleCanNotBeRemoved.class)
    ResponseEntity<String> handleAccountRolesLimitException(RoleCanNotBeRemoved e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


}
