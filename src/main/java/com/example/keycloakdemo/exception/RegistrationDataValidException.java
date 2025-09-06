package com.example.keycloakdemo.exception;

public class RegistrationDataValidException extends RuntimeException {
    public RegistrationDataValidException(String message) {
        super(message);
    }
}
