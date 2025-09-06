package com.example.keycloakdemo.service;

import com.example.keycloakdemo.exception.FailedCreateUserException;
import com.example.keycloakdemo.exception.ParseMessageException;
import com.example.keycloakdemo.exception.RegistrationDataValidException;
import com.example.keycloakdemo.web.dto.request.RegistrationRequest;
import com.example.keycloakdemo.web.dto.response.RegistrationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegistrationService {

    private final Keycloak keycloak;
    private final ObjectMapper objectMapper;

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    public RegistrationService(Keycloak keycloak, ObjectMapper objectMapper) {
        this.keycloak = keycloak;
        this.objectMapper = objectMapper;
    }

    public RegistrationResponse registration(RegistrationRequest request) throws ParseMessageException, FailedCreateUserException {
        if (!checkRoleValid(request.getRole())) {
            throw new RegistrationDataValidException("Invalid role: only USER or ADMIN or MODERATOR allowed");
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEnabled(true);
        user.setEmailVerified(false);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(request.getPassword());
        user.setCredentials(Collections.singletonList(passwordCred));

        String userId = saveUserInKeycloak(user);

        RoleRepresentation roleRepresentation = keycloak.realm(realm)
                .roles()
                .get(request.getRole())
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRepresentation));

        return new RegistrationResponse("Role " + request.getRole() + " assigned to user " + request.getUsername());
    }

    private boolean checkRoleValid(String role) {
        role = role.toUpperCase();
        return role.equals("USER") || role.equals("ADMIN") || role.equals("MODERATOR");
    }

    private String saveUserInKeycloak(UserRepresentation user) {
        try (Response response = keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() != 201) {
                String errorMessage = "Failed to create user.";
                if (response.hasEntity()) {
                    String message = getErrorMessageFromResponse(response);
                    errorMessage += " Details: " + message;
                }
                throw new FailedCreateUserException(errorMessage);
            }
            return CreatedResponseUtil.getCreatedId(response);
        } catch (Exception e) {
            throw new FailedCreateUserException("Failed to create user due to an unexpected error: "+e.getMessage());
        }
    }

    private String getErrorMessageFromResponse(Response response) {
        try {
            String responseBody = response.readEntity(String.class);
            JsonNode errorJson = objectMapper.readTree(responseBody);
            return errorJson.path("errorMessage").asText("Unknown error from Keycloak");
        } catch (Exception e) {
            throw new ParseMessageException("Failed to parse error message: " + e.getMessage());
        }
    }
}
