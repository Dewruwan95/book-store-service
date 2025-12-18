package com.application.bookstore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ValidationException.class);

    public ValidationException(String attributeName) {
        super(attributeName + " is required");
        logging(attributeName);
    }

    public ValidationException(String attributeName, String message) {
        super(attributeName + " - " + message);
        logging(attributeName);
    }

    private void logging(String attributeName) {
        logger.warn("Validation failed for: {}", attributeName);
    }
}
