package com.example.matrimony.dto;


import com.example.matrimony.entity.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DiscountUpdateRequest {

    private DiscountType discountType;   // PERCENTAGE / FLAT
    private Long discountValue; // 10% OR 500
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String discountStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String discountEnd;

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
}