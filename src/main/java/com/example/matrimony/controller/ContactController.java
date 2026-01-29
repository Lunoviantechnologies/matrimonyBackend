package com.example.matrimony.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.ContactRequest;
import com.example.matrimony.service.EmailService;
import com.example.matrimony.service.Notificationadminservice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin("*")
public class ContactController {

    private final EmailService emailService;
    @Autowired
    private Notificationadminservice adminNotificationService;


    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    // GET API
    @GetMapping("/All")
    public ResponseEntity<String> getContactStatus() {
        return ResponseEntity.ok("Contact API is working successfully!");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendContact(
            @Valid @RequestBody ContactRequest contactRequest) {

        // ================== SEND EMAIL ==================
        emailService.sendContactMessage(contactRequest);

        // ================== 🔔 ADMIN NOTIFICATION ==================
        String adminMessage =
                "New contact message received from " +
                contactRequest.getName() +
                " (" + contactRequest.getEmail() + ")";

        adminNotificationService.notifyAdmin(
                "CONTACT_MESSAGE",
                adminMessage,
                Map.of(
                        "name", contactRequest.getName(),
                        "email", contactRequest.getEmail(),
                        "message", contactRequest.getMessage()
                )
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body("Message sent successfully!");
    }

}