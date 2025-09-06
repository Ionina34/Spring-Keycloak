package com.example.keycloakdemo.web.controller;

import com.example.keycloakdemo.exception.FailedCreateUserException;
import com.example.keycloakdemo.exception.ParseMessageException;
import com.example.keycloakdemo.exception.RegistrationDataValidException;
import com.example.keycloakdemo.service.RegistrationService;
import com.example.keycloakdemo.web.dto.request.RegistrationRequest;
import com.example.keycloakdemo.web.dto.response.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SecurityController {

    private RegistrationService registrationService;

    @Autowired
    public SecurityController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> assignRole(@RequestBody RegistrationRequest request)
            throws ParseMessageException,
            FailedCreateUserException,
            RegistrationDataValidException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registrationService.registration(request));
    }
}
