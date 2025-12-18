package com.application.bookstore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeAlreadyExistsException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(AttributeAlreadyExistsException.class);

    public AttributeAlreadyExistsException(String entityName, String attributeName, String attributeValue) {
        super(entityName + " already exists with " + attributeName + ": " + attributeValue);
        logger.warn("Validation failed with duplicate: {}", attributeName);
    }
}
