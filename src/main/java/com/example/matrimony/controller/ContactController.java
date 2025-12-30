package com.example.matrimony.controller;

import com.example.matrimony.dto.ContactRequest;
import com.example.matrimony.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin("*")
public class ContactController {

    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    // GET API
    @GetMapping("/All")
    public ResponseEntity<String> getContactStatus() {
        return ResponseEntity.ok("Contact API is working successfully!");
    }

    // POST API
    @PostMapping("/send")
    public ResponseEntity<String> sendContact(@Valid @RequestBody ContactRequest contactRequest) {
        emailService.sendContactMessage(contactRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Message sent successfully!");
    }
}