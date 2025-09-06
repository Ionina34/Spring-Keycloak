package com.example.keycloakdemo.web.dto.response;

public class ErrorRegistrationResponse {
    private String message;

    public ErrorRegistrationResponse() {
    }

    public ErrorRegistrationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
