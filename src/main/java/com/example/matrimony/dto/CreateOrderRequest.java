package com.example.matrimony.dto;

public class CreateOrderRequest {
    private Long profileId;
    private String planCode;

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }
}
