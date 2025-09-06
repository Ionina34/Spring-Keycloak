package com.example.keycloakdemo.exception;

public class FailedCreateUserException extends RuntimeException {
    public FailedCreateUserException(String message) {
        super(message);
    }
}
