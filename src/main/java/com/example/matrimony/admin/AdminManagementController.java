package com.example.matrimony.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/manage")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminManagementController {

    private final AdminManagementService service;

    public AdminManagementController(AdminManagementService service) {
        this.service = service;
    }

    // ------------------------------------
    // ✅ CREATE NEW ADMIN
    // ------------------------------------
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(
            @Valid @RequestBody CreateAdminRequest request) {

        service.createAdmin(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Admin created successfully");
    }

    // ------------------------------------
    // ✅ GET ALL ADMINS
    // ------------------------------------
    @GetMapping("/all")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(service.getAllAdmins());
    }

 // ------------------------------------
 // ✅ DELETE ADMIN (BY ID) – PRODUCTION READY
 // ------------------------------------
 @DeleteMapping("/delete/{adminId}")
 public ResponseEntity<?> deleteAdmin(@PathVariable("adminId") Long adminId) {

     if (adminId == null || adminId <= 0) {
         return ResponseEntity
                 .badRequest()
                 .body(Map.of(
                         "error", "Invalid adminId",
                         "message", "adminId must be a positive number"
                 ));
     }

     service.deleteAdmin(adminId);

     return ResponseEntity.ok(
             Map.of(
                     "message", "Admin deleted successfully",
                     "adminId", adminId
             )
     );
 }

}
