package com.example.matrimony.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.repository.AdminRepository;
import com.example.matrimony.service.Notificationadminservice;

@RestController
@RequestMapping("/api/admin/notifications")
@CrossOrigin(origins = "*")
public class NotificationadminController {

    private final Notificationadminservice service;
    private final AdminRepository adminRepository;

    public NotificationadminController(Notificationadminservice service,
                                       AdminRepository adminRepository) {
        this.service = service;
        this.adminRepository = adminRepository;
    }

    // ------------------------------
    // GET ALL NOTIFICATIONS
    // ------------------------------
    @GetMapping("/All")
    public ResponseEntity<?> listNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().body("Admin not authenticated");
        }

        // ✅ Get numeric admin ID safely from email
        Admin admin = adminRepository.findByEmailId(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Long adminId = admin.getAdminId();

        return ResponseEntity.ok(
                service.getNotifications(adminId, PageRequest.of(page, size))
        );
    }

    // ------------------------------
    // GET UNREAD COUNT
    // ------------------------------
    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(Principal principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().body("Admin not authenticated");
        }

        Admin admin = adminRepository.findByEmailId(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Long adminId = admin.getAdminId();

        Map<String, Long> count =
                Collections.singletonMap("count", service.unreadCount(adminId));

        return ResponseEntity.ok(count);
    }

    // ------------------------------
    // MARK ONE READ
    // ------------------------------
    @PostMapping("/read/{id}")
    public ResponseEntity<?> markRead(
            @PathVariable Long id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().body("Admin not authenticated");
        }

        Admin admin = adminRepository.findByEmailId(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Long adminId = admin.getAdminId();

        service.markAsRead(id, adminId);
        return ResponseEntity.ok().build();
    }

    // ------------------------------
    // MARK ALL READ
    // ------------------------------
    @PostMapping("/mark-all-read")
    public ResponseEntity<?> markAllRead(Principal principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().body("Admin not authenticated");
        }

        Admin admin = adminRepository.findByEmailId(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Long adminId = admin.getAdminId();

        service.markAllRead(adminId);
        return ResponseEntity.ok().build();
    }
}
