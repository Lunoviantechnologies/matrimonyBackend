package com.example.matrimony.referral;

import java.math.BigDecimal;

public class ReferralSummaryDto {

    private String referralCode;
    private String referralLink;
    private long completedReferrals;
    private long pendingReferrals;
    private long totalReferralsNeeded;
    private BigDecimal rewardBalance;
    private boolean eligibleForReward;
    private long remainingForNextReward;
    private String signupReferralCode;

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferralLink() {
        return referralLink;
    }

    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }

    public long getCompletedReferrals() {
        return completedReferrals;
    }

    public void setCompletedReferrals(long completedReferrals) {
        this.completedReferrals = completedReferrals;
    }

    public long getPendingReferrals() {
        return pendingReferrals;
    }

    public void setPendingReferrals(long pendingReferrals) {
        this.pendingReferrals = pendingReferrals;
    }

    public long getTotalReferralsNeeded() {
        return totalReferralsNeeded;
    }

    public void setTotalReferralsNeeded(long totalReferralsNeeded) {
        this.totalReferralsNeeded = totalReferralsNeeded;
    }

    public BigDecimal getRewardBalance() {
        return rewardBalance;
    }

    public void setRewardBalance(BigDecimal rewardBalance) {
        this.rewardBalance = rewardBalance;
    }

    public boolean isEligibleForReward() {
        return eligibleForReward;
    }

    public void setEligibleForReward(boolean eligibleForReward) {
        this.eligibleForReward = eligibleForReward;
    }

    public long getRemainingForNextReward() {
        return remainingForNextReward;
    }

    public void setRemainingForNextReward(long remainingForNextReward) {
        this.remainingForNextReward = remainingForNextReward;
    }
    public String getSignupReferralCode() {
        return signupReferralCode;
    }

    public void setSignupReferralCode(String signupReferralCode) {
        this.signupReferralCode = signupReferralCode;
    }
}