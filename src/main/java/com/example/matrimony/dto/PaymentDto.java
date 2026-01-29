package com.example.matrimony.dto;

import java.time.LocalDateTime;

public class PaymentDto {

    private Long id;
    private Long userId;
    private String name;

    private String planCode;
    private Long amount; // RUPEES ✅ only once
    private String currency;
    private String status;

    private String razorpayOrderId;
    private String razorpayPaymentId;

    private String paymentMode;
    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime premiumEnd;
    private String expiryMessage;

    // ================= GETTERS & SETTERS =================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

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