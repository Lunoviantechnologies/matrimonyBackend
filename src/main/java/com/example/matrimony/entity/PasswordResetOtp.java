package com.example.matrimony.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otp")
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String otp;
    private LocalDateTime expiresAt;
    private boolean verified;
    
    public enum OtpPurpose {
        PASSWORD_RESET,
        EMAIL_VERIFICATION
    }
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose;

    
 // getters and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public OtpPurpose getPurpose() {
		return purpose;
	}
	public void setPurpose(OtpPurpose purpose) {
		this.purpose = purpose;
	}
	@Override
	public String toString() {
	    return "PasswordResetOtp [id=" + id +
	            ", email=" + email +
	            ", otp=" + otp +
	            ", expiresAt=" + expiresAt +
	            ", verified=" + verified +
	            ", purpose=" + purpose + "]";
	}

    
	
    
    
}