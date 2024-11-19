package com.featherbook.featherbookbackend.usermanagement.domain.exceptions;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
