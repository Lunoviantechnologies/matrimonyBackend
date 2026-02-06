package com.example.matrimony.dto;

import com.example.matrimony.entity.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UpdateSubscriptionPlanFullRequest {

	 private Long id;
    private Long festivalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd")// null = disable festival pricing
   
    private String festivalStart; // yyyy-MM-dd'T'HH:mm
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String festivalEnd;

    private String planCode;
    private String planName;
    private Integer durationMonths;
    private Long priceRupees;
    
    private DiscountType discountType;
    private Long discountValue;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String discountStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String discountEnd;
    
    private boolean active;
   

    private Integer contacts;
    private Boolean chat;
    private String astroSupport;
    private Boolean relationshipManager;
    private String benefit;

    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFestivalPrice() { return festivalPrice; }
    public void setFestivalPrice(Long festivalPrice) {
        this.festivalPrice = festivalPrice;
    }

    public String getFestivalStart() { return festivalStart; }
    public void setFestivalStart(String festivalStart) {
        this.festivalStart = festivalStart;
    }

    public String getFestivalEnd() { return festivalEnd; }
    public void setFestivalEnd(String festivalEnd) {
        this.festivalEnd = festivalEnd;
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
	public Integer getDurationMonths() {
		return durationMonths;
	}
	public void setDurationMonths(Integer durationMonths) {
		this.durationMonths = durationMonths;
	}
	public Long getPriceRupees() {
		return priceRupees;
	}
	public void setPriceRupees(Long priceRupees) {
		this.priceRupees = priceRupees;
	}
	public DiscountType getDiscountType() {
		return discountType;
	}
	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}
	public Long getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(Long discountValue) {
		this.discountValue = discountValue;
	}
	public String getDiscountStart() {
		return discountStart;
	}
	public void setDiscountStart(String discountStart) {
		this.discountStart = discountStart;
	}
	public String getDiscountEnd() {
		return discountEnd;
	}
	public void setDiscountEnd(String discountEnd) {
		this.discountEnd = discountEnd;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	

	public Integer getContacts() {
		return contacts;
	}
	public void setContacts(Integer contacts) {
		this.contacts = contacts;
	}
	public Boolean getChat() {
		return chat;
	}
	public void setChat(Boolean chat) {
		this.chat = chat;
	}
	public String getAstroSupport() {
		return astroSupport;
	}
	public void setAstroSupport(String astroSupport) {
		this.astroSupport = astroSupport;
	}
	public Boolean getRelationshipManager() {
		return relationshipManager;
	}
	public void setRelationshipManager(Boolean relationshipManager) {
		this.relationshipManager = relationshipManager;
	}
	public String getBenefit() {
		return benefit;
	}
	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}
	@Override
	public String toString() {
		return "FestivalPriceUpdateRequest [festivalPrice=" + festivalPrice + ", festivalStart=" + festivalStart
				+ ", festivalEnd=" + festivalEnd + ", planCode=" + planCode + ", planName=" + planName
				+ ", durationMonths=" + durationMonths + ", priceRupees=" + priceRupees + ", discountType="
				+ discountType + ", discountValue=" + discountValue + ", discountStart=" + discountStart
				+ ", discountEnd=" + discountEnd + "]";
	}
    
}