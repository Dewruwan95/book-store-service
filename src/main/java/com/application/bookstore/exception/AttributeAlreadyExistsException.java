package com.application.bookstore.exception;

public class AttributeAlreadyExistsException extends RuntimeException {

    public AttributeAlreadyExistsException(String entityName, String attributeName, String attributeValue) {
        super(entityName + " already exists with " + attributeName + ": " + attributeValue);
    }
}
