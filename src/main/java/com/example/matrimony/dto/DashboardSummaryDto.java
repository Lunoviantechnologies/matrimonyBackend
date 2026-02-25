package com.example.matrimony.dto;

import java.util.List;
import com.example.matrimony.referral.ReferralSummaryDto;

public class DashboardSummaryDto {

    private List<ProfileCardDto> newMatches;
    private List<ProfileCardDto> premiumMatches;
    private List<ProfileCardDto> recommendedMatches;

    private long receivedCount;
    private long acceptedCount;
    private long rejectedCount;
    private long sentCount;

    private ReferralSummaryDto referral;
    private SubscriptionPlanResponse subscription;

    public List<ProfileCardDto> getNewMatches() {
        return newMatches;
    }

    public void setNewMatches(List<ProfileCardDto> newMatches) {
        this.newMatches = newMatches;
    }

    public List<ProfileCardDto> getPremiumMatches() {
        return premiumMatches;
    }

    public void setPremiumMatches(List<ProfileCardDto> premiumMatches) {
        this.premiumMatches = premiumMatches;
    }

    public List<ProfileCardDto> getRecommendedMatches() {
        return recommendedMatches;
    }

    public void setRecommendedMatches(List<ProfileCardDto> recommendedMatches) {
        this.recommendedMatches = recommendedMatches;
    }

    public long getReceivedCount() {
        return receivedCount;
    }

    public void setReceivedCount(long receivedCount) {
        this.receivedCount = receivedCount;
    }

    public long getAcceptedCount() {
        return acceptedCount;
    }

    public void setAcceptedCount(long acceptedCount) {
        this.acceptedCount = acceptedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getSentCount() {
        return sentCount;
    }

    public void setSentCount(long sentCount) {
        this.sentCount = sentCount;
    }

    public ReferralSummaryDto getReferral() {
        return referral;
    }

    public void setReferral(ReferralSummaryDto referral) {
        this.referral = referral;
    }

    public SubscriptionPlanResponse getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionPlanResponse subscription) {
        this.subscription = subscription;
    }
}