package com.example.keycloakdemo.web.controller;

import com.example.keycloakdemo.exception.FailedCreateUserException;
import com.example.keycloakdemo.exception.ParseMessageException;
import com.example.keycloakdemo.exception.RegistrationDataValidException;
import com.example.keycloakdemo.web.dto.response.ErrorRegistrationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ParseMessageException.class)
    public ResponseEntity<ErrorRegistrationResponse> parseMessageException(ParseMessageException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorRegistrationResponse(e.getMessage()));
    }

    @ExceptionHandler(FailedCreateUserException.class)
    public ResponseEntity<ErrorRegistrationResponse> failedCreateUserException(FailedCreateUserException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorRegistrationResponse(e.getMessage()));
    }

    @ExceptionHandler(RegistrationDataValidException.class)
    public ResponseEntity<ErrorRegistrationResponse> registrationDataValidException(RegistrationDataValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRegistrationResponse(e.getMessage()));
    }
}
