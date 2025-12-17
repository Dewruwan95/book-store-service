package com.application.bookstore.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String entity, String email) {
        super(entity + " already exists with email: " + email);
    }
}
