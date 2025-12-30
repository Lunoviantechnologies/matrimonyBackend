package com.example.matrimony.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.PasswordResetOtp;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.PasswordResetOtpRepository;
import com.example.matrimony.repository.ProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class PasswordResetService {

    private final PasswordResetOtpRepository otpRepo;
    private final ProfileRepository profileRepo;
    private final BCryptPasswordEncoder encoder;

    public PasswordResetService(
            PasswordResetOtpRepository otpRepo,
            ProfileRepository profileRepo,
            BCryptPasswordEncoder encoder) {
        this.otpRepo = otpRepo;
        this.profileRepo = profileRepo;
        this.encoder = encoder;
    }

    // ✅ SINGLE, CORRECT METHOD
    public void resetPassword(String email,
                              String newPassword,
                              String confirmPassword) {

        // 1️⃣ confirm password validation
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2️⃣ get latest PASSWORD_RESET OTP
        PasswordResetOtp otp = otpRepo
                .findTopByEmailAndPurposeOrderByExpiresAtDesc(
                        email, PasswordResetOtp.OtpPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!otp.isVerified()) {
            throw new RuntimeException("OTP not verified");
        }

        // 3️⃣ update password
        Profile profile = profileRepo.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setCreatePassword(encoder.encode(newPassword));
        profileRepo.save(profile);

        // 4️⃣ cleanup OTP
        otpRepo.delete(otp);
    }
    
    
    @Transactional
    public void changePassword(Long profileId,
                               String currentPassword,
                               String newPassword,
                               String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        Profile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // verify old password
        if (!encoder.matches(currentPassword, profile.getCreatePassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        profile.setCreatePassword(encoder.encode(newPassword));
        profileRepo.save(profile);
    }

}