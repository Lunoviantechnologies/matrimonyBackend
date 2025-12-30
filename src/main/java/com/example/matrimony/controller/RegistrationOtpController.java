package com.example.matrimony.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.dto.EmailRequest;
import com.example.matrimony.dto.VerifyOtpRequest;
import com.example.matrimony.service.OtpService;

@RestController
@RequestMapping("/api/auth/register")
@CrossOrigin
public class RegistrationOtpController {

    private final OtpService otpService;

    public RegistrationOtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendRegistrationOtp(@RequestBody EmailRequest req) {
        otpService.sendRegistrationOtp(req.getEmail());
        return ResponseEntity.ok("Registration OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyRegistrationOtp(@RequestBody VerifyOtpRequest req) {
        otpService.verifyRegistrationOtp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok("Email verified successfully");
    }
}