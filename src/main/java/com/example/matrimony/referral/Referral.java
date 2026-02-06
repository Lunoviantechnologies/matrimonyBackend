package com.example.matrimony.referral;

import com.example.matrimony.entity.Profile;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "referrals")
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_profile_id", nullable = false)
    private Profile referrer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_profile_id")
    private Profile referred;

    @Column(name = "referred_email", nullable = false)
    private String referredEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReferralStatus status = ReferralStatus.PENDING;

    @Column(name = "reward_applied", nullable = false)
    private boolean rewardApplied = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "rewarded_at")
    private LocalDateTime rewardedAt;

    public Long getId() {
        return id;
    }

    public Profile getReferrer() {
        return referrer;
    }

    public void setReferrer(Profile referrer) {
        this.referrer = referrer;
    }

    public Profile getReferred() {
        return referred;
    }

    public void setReferred(Profile referred) {
        this.referred = referred;
    }

    public String getReferredEmail() {
        return referredEmail;
    }

    public void setReferredEmail(String referredEmail) {
        this.referredEmail = referredEmail;
    }

    public ReferralStatus getStatus() {
        return status;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public boolean isRewardApplied() {
        return rewardApplied;
    }

    public void setRewardApplied(boolean rewardApplied) {
        this.rewardApplied = rewardApplied;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getRewardedAt() {
        return rewardedAt;
    }

    public void setRewardedAt(LocalDateTime rewardedAt) {
        this.rewardedAt = rewardedAt;
    }
}

