package com.example.matrimony.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {
	
	@Autowired
	private ProfileRepository profileRepository;
	

    private final NotificationService service;

    public NotificationRestController(NotificationService service) {
        this.service = service;
    }

    // ✅ List notifications with optional page & size, uses Principal or query param
    @GetMapping("/GetAll")
    public ResponseEntity<?> listNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            @RequestParam(required = false) Long userId,
            Principal principal) {

        if (userId == null) {
            if (principal == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }
            userId = Long.parseLong(principal.getName());
        }

        return ResponseEntity.ok(service.getNotifications(userId, PageRequest.of(page, size)));
    }

    // ✅ Get unread count
    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(
            @RequestParam(required = false) Long userId,
            Principal principal) {

        if (userId == null) {
            if (principal == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }
            userId = Long.parseLong(principal.getName());
        }

        Map<String, Long> count = Collections.singletonMap("count", service.unreadCount(userId));
        return ResponseEntity.ok(count);
    }

    // ✅ Mark a single notification as read
    @PostMapping("/read/{id}")
    public ResponseEntity<?> markRead(
            @PathVariable Long id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        Profile user = profileRepository
                .findByEmailId(principal.getName())   
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        service.markAsRead(id, userId);
        return ResponseEntity.ok("Notification marked as read");
    }



    // ✅ Mark all notifications as read
    @PostMapping("/mark-all-read")
    public ResponseEntity<?> markAllRead(
            @RequestParam(required = false) Long userId,
            Principal principal) {

        if (userId == null) {
            if (principal == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }
            userId = Long.parseLong(principal.getName());
        }

        service.markAllRead(userId);
        return ResponseEntity.ok().build();
    }
}
