package com.example.matrimony.controller;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }
   
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody Admin req) {

        authService.createAdmin(req);

        return ResponseEntity.ok(
                java.util.Map.of("message", "Admin created successfully")
        );
    }
}

