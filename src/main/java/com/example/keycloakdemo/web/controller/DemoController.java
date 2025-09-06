
package com.example.keycloakdemo.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public void getUserInfo() {
        System.out.println("User info");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void getAdminInfo() {
        System.out.println("Admin info");
    }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR')")
    public void getModeratorInfo() {
        System.out.println("Moderator info");
    }
}
