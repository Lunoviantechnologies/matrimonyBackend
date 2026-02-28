package com.example.matrimony.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SubscriptionPlanResponse {

    private String planCode;
    private String planName;
    private LocalDateTime premiumStart;
    private LocalDateTime premiumEnd;
    private BigDecimal amount;
    private String expiryMessage;

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getExpiryMessage() {
		return expiryMessage;
	}
	public void setExpiryMessage(String expiryMessage) {
		this.expiryMessage = expiryMessage;
	}
    
}