package com.example.matrimony.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.ChangePasswordRequest;
import com.example.matrimony.dto.ForgotPasswordRequest;
import com.example.matrimony.dto.ResetPasswordRequest;
import com.example.matrimony.dto.VerifyOtpRequest;
import com.example.matrimony.service.OtpService;
import com.example.matrimony.service.PasswordResetService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class ForgotPasswordController {

    private final OtpService otpService;
    private final PasswordResetService resetService;

    public ForgotPasswordController(OtpService otpService,
                                    PasswordResetService resetService) {
        this.otpService = otpService;
        this.resetService = resetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest req) {
        otpService.sendOtp(req.getEmail());
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verify(@RequestBody VerifyOtpRequest req) {
        otpService.verifyOtp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {

        resetService.resetPassword(
                req.getEmail(),
                req.getNewPassword(),
                req.getConfirmPassword()
        );

        return ResponseEntity.ok("Password updated successfully");
    }

    //  LOGGED-IN USER CHANGE PASSWORD
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest req) {

        try {

            resetService.changePassword(
                    id,
                    req.getCurrentPassword(),
                    req.getNewPassword(),
                    req.getConfirmPassword()
            );

            return ResponseEntity.ok("Password updated successfully");

        } catch (RuntimeException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

}

