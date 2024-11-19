package com.featherbook.featherbookbackend.library.domain.exceptions;

public class InvalidSubscriptionException extends RuntimeException {
    public InvalidSubscriptionException(String message) {
        super(message);
    }
}