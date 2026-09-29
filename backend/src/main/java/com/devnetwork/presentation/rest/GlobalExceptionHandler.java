package com.devnetwork.presentation.rest;

import com.devnetwork.application.auth.InvalidCredentialsException;
import com.devnetwork.domain.connection.ConnectionActionNotAllowedException;
import com.devnetwork.domain.connection.ConnectionAlreadyExistsException;
import com.devnetwork.domain.connection.ConnectionNotFoundException;
import com.devnetwork.domain.user.EmailAlreadyUsedException;
import com.devnetwork.domain.user.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Translates domain exceptions into HTTP responses (RFC 7807 problem details).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, ConnectionNotFoundException.class})
    ProblemDetail handleNotFound(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({EmailAlreadyUsedException.class, ConnectionAlreadyExistsException.class})
    ProblemDetail handleConflict(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** A unique constraint hit by two concurrent requests, e.g. both users sending a request at once. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleIntegrityViolation(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "This conflicts with existing data. Please refresh and try again.");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ConnectionActionNotAllowedException.class)
    ProblemDetail handleForbidden(ConnectionActionNotAllowedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleBadRequest(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
