package com.application.bookstore.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException e){

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Entity not found");
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("reason",e.getMessage());
        problemDetail.setProperty("severity","ERROR");

        return problemDetail;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidationException(ValidationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("reason", e.getMessage());
        problemDetail.setProperty("severity", "ERROR");

        return problemDetail;
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Email Already Exists");
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("reason", e.getMessage());
        problemDetail.setProperty("severity", "ERROR");

        return problemDetail;
    }
}
