package com.example.matrimony.dto;

public class SubscriptionPlanResponse {

    private String planCode;
    private String planName;
    private Integer durationMonths;
    private Long priceRupees;

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Long getPriceRupees() { return priceRupees; }
    public void setPriceRupees(Long priceRupees) {
        this.priceRupees = priceRupees;
    }
}