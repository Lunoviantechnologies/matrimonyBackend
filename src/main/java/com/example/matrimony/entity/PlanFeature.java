package com.example.matrimony.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "plan_features")
public class PlanFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer contacts;
    private Boolean chat;
    private String astroSupport;
    private Boolean relationshipManager;
    private String benefit;

    @OneToOne
    @JoinColumn(name = "plan_id", nullable = false, unique = true)
    @JsonIgnore   // 🔥 VERY IMPORTANT
    private SubscriptionPlan plan;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public SubscriptionPlan getPlan() {
		return plan;
	}

	public void setPlan(SubscriptionPlan plan) {
		this.plan = plan;
	}

    
}
