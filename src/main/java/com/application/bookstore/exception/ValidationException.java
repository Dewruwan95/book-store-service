package com.application.bookstore.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String attributeName) {
        super(attributeName + " is required");
    }

    public ValidationException(String attributeName, String message) {
        super(attributeName + " - " + message);
    }
}
