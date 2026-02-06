package com.example.matrimony.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "subscription_plans",
    indexes = {
        @Index(name = "idx_plan_code", columnList = "plan_code", unique = true)
    }
)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "plan", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PlanFeature planFeature;

    @Column(name = "plan_code", nullable = false, unique = true)
    private String planCode;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(name = "price_rupees", nullable = false)
    private BigDecimal priceRupees;

    @Column(name = "festival_price_rupees")
    private BigDecimal festivalPrice;

    @Column(name = "festival_start")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime festivalStart;

    @Column(name = "festival_end")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime festivalEnd;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private BigDecimal discountValue;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime discountStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime discountEnd;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public BigDecimal getPriceRupees() {
		return priceRupees;
	}
	public void setPriceRupees(BigDecimal priceRupees) {
		this.priceRupees = priceRupees;
	}
	public BigDecimal getFestivalPrice() {
		return festivalPrice;
	}
	public void setFestivalPrice(BigDecimal festivalPrice) {
		this.festivalPrice = festivalPrice;
	}
	public LocalDateTime getFestivalStart() {
		return festivalStart;
	}
	public void setFestivalStart(LocalDateTime festivalStart) {
		this.festivalStart = festivalStart;
	}
	public LocalDateTime getFestivalEnd() {
		return festivalEnd;
	}
	public void setFestivalEnd(LocalDateTime festivalEnd) {
		this.festivalEnd = festivalEnd;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public DiscountType getDiscountType() {
		return discountType;
	}
	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	public LocalDateTime getDiscountStart() {
		return discountStart;
	}
	public void setDiscountStart(LocalDateTime discountStart) {
		this.discountStart = discountStart;
	}
	public LocalDateTime getDiscountEnd() {
		return discountEnd;
	}
	public void setDiscountEnd(LocalDateTime discountEnd) {
		this.discountEnd = discountEnd;
	}
	
	public PlanFeature getPlanFeature() {
	    return planFeature;
	}

	public void setPlanFeature(PlanFeature planFeature) {
	    this.planFeature = planFeature;
	}

	@Override
	public String toString() {
		return "SubscriptionPlan [id=" + id + ", planCode=" + planCode + ", planName=" + planName + ", durationMonths="
				+ durationMonths + ", priceRupees=" + priceRupees + ", festivalPriceRupees=" + festivalPrice
				+ ", festivalStart=" + festivalStart + ", festivalEnd=" + festivalEnd + ", active=" + active
				+ ", updatedAt=" + updatedAt + ", discountType=" + discountType + ", discountValue=" + discountValue
				+ ", discountStart=" + discountStart + ", discountEnd=" + discountEnd + "]";
	}
    
}