
package com.example.matrimony.service;

import com.example.matrimony.dto.ContactRequest;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    public ContactService() {}

    @Transactional
    public void handleContact(ContactRequest req) {

        // 🔐 Validate email before using it anywhere
        String email = req.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        email = email.trim();

        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();   // STRICT validation
        } catch (AddressException e) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }

        // ✅ If Contact entity exists, use it
        /*
        Contact c = new Contact();
        c.setName(req.getName());
        c.setEmail(email);
        c.setPhoneNumber(req.getPhoneNumber());
        c.setMessage(req.getMessage());
        contactRepo.save(c);
        */

        // For now, just log
        System.out.println("Received contact request from: " + email);
    }
}

