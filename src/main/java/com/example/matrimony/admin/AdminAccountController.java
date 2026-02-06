package com.example.matrimony.admin;

import jakarta.validation.Valid;
import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/account")
public class AdminAccountController {

    private final AdminManagementService service;

    public AdminAccountController(AdminManagementService service) {
        this.service = service;
    }

    // ------------------------------------
    // ✅ CHANGE PASSWORD
    // ------------------------------------
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized request");
        }

        service.changePassword(principal.getName(), request);

        return ResponseEntity.ok("Password changed successfully");
    }
}
