package com.example.matrimony.repository;

import com.example.matrimony.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findTopByEmailOrderByExpiresAtDesc(String email);
    
    Optional<PasswordResetOtp>
    findTopByEmailAndPurposeOrderByExpiresAtDesc(
            String email,
            PasswordResetOtp.OtpPurpose purpose);

}