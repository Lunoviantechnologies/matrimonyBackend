package com.example.matrimony.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentSummaryDto {

    private Long id;
    private String name;
    private String planCode;
    private String planName;

    // money (RUPEES)
    private BigDecimal amount;
    private String currency;
    private String status;

    private String razorpayOrderId;
    private String razorpayPaymentId;

    private LocalDateTime premiumStart;
    private LocalDateTime premiumEnd;
    private String expiryMessage;

    private LocalDateTime createdAt;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public LocalDateTime getPremiumStart() {
        return premiumStart;
    }

    public void setPremiumStart(LocalDateTime premiumStart) {
        this.premiumStart = premiumStart;
    }

    public LocalDateTime getPremiumEnd() {
        return premiumEnd;
    }

    public void setPremiumEnd(LocalDateTime premiumEnd) {
        this.premiumEnd = premiumEnd;
    }

    public String getExpiryMessage() {
        return expiryMessage;
    }

    public void setExpiryMessage(String expiryMessage) {
        this.expiryMessage = expiryMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
