package com.application.bookstore.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Define constants for property keys
    private static final String PROPERTY_TIMESTAMP = "timestamp";
    private static final String PROPERTY_REASON = "reason";
    private static final String PROPERTY_SEVERITY = "severity";

    // define constants for titles
    private static final String TITLE_ENTITY_NOT_FOUND = "Entity not found";
    private static final String TITLE_VALIDATION_ERROR = "Validation Error";
    private static final String TITLE_EMAIL_EXISTS = "Attribute Already Exists";
    private static final String SEVERITY_ERROR = "ERROR";

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle(TITLE_ENTITY_NOT_FOUND);
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now().toString());
        problemDetail.setProperty(PROPERTY_REASON, e.getMessage());
        problemDetail.setProperty(PROPERTY_SEVERITY, SEVERITY_ERROR);

        return problemDetail;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidationException(ValidationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle(TITLE_VALIDATION_ERROR);
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now().toString());
        problemDetail.setProperty(PROPERTY_REASON, e.getMessage());
        problemDetail.setProperty(PROPERTY_SEVERITY, SEVERITY_ERROR);

        return problemDetail;
    }

    @ExceptionHandler(AttributeAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(AttributeAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle(TITLE_EMAIL_EXISTS);
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now().toString());
        problemDetail.setProperty(PROPERTY_REASON, e.getMessage());
        problemDetail.setProperty(PROPERTY_SEVERITY, SEVERITY_ERROR);

        return problemDetail;
    }
}
