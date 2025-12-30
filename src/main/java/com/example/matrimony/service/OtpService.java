package com.example.matrimony.service;

import com.example.matrimony.entity.PasswordResetOtp;
import com.example.matrimony.repository.PasswordResetOtpRepository;
import com.example.matrimony.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final PasswordResetOtpRepository otpRepo;
    private final ProfileRepository profileRepo;
    private final EmailService emailService;

    public OtpService(PasswordResetOtpRepository otpRepo,
                      ProfileRepository profileRepo,
                      EmailService emailService) {
        this.otpRepo = otpRepo;
        this.profileRepo = profileRepo;
        this.emailService = emailService;
    }

    /* =========================================================
       FORGOT PASSWORD OTP  (EXISTING – WORKING)
       ========================================================= */

    public void sendOtp(String email) {

        if (!profileRepo.existsByEmailId(email)) {
            throw new RuntimeException("Email not registered");
        }

        String otp = generateOtp();

        PasswordResetOtp entity = new PasswordResetOtp();
        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setVerified(false);
        entity.setPurpose(PasswordResetOtp.OtpPurpose.PASSWORD_RESET);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        otpRepo.save(entity);
        emailService.sendOtp(email, otp);
    }

    public void verifyOtp(String email, String otp) {

        PasswordResetOtp saved = otpRepo
                .findTopByEmailAndPurposeOrderByExpiresAtDesc(
                        email, PasswordResetOtp.OtpPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (saved.isVerified()) {
            throw new RuntimeException("OTP already verified");
        }

        if (saved.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!saved.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        saved.setVerified(true);
        otpRepo.save(saved);
    }

    /* =========================================================
       REGISTRATION EMAIL VERIFICATION OTP  (NEW)
       ========================================================= */

    public void sendRegistrationOtp(String email) {

        // ❌ User must NOT already exist
        if (profileRepo.existsByEmailId(email)) {
            throw new RuntimeException("Email already registered");
        }

        String otp = generateOtp();

        PasswordResetOtp entity = new PasswordResetOtp();
        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setVerified(false);
        entity.setPurpose(PasswordResetOtp.OtpPurpose.EMAIL_VERIFICATION);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        otpRepo.save(entity);
        emailService.sendOtp(email, otp);
    }

    public void verifyRegistrationOtp(String email, String otp) {

        PasswordResetOtp saved = otpRepo
                .findTopByEmailAndPurposeOrderByExpiresAtDesc(
                        email, PasswordResetOtp.OtpPurpose.EMAIL_VERIFICATION)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (saved.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!saved.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        saved.setVerified(true);
        otpRepo.save(saved);
    }



    /* =========================================================
       COMMON OTP GENERATOR
       ========================================================= */
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}