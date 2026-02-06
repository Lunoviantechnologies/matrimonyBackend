package com.example.matrimony.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;

public class PaymentDto {

    private Long id;
    private Long userId;
    private String name;

    private String planCode;
    private BigDecimal amount; // RUPEES  only once
    private String currency;
    private String status;

    private String razorpayOrderId;
    private String razorpayPaymentId;


    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "transaction_id") 
    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime premiumEnd;
    private String expiryMessage;
    private String planName;
    private LocalDateTime premiumStart;

    // ================= GETTERS & SETTERS =================

    public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public LocalDateTime getPremiumStart() {
		return premiumStart;
	}
	public void setPremiumStart(LocalDateTime premiumStart) {
		this.premiumStart = premiumStart;
	}
	public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getPremiumEnd() { return premiumEnd; }
    public void setPremiumEnd(LocalDateTime premiumEnd) { this.premiumEnd = premiumEnd; }

    public String getExpiryMessage() { return expiryMessage; }
    public void setExpiryMessage(String expiryMessage) { this.expiryMessage = expiryMessage; }
}