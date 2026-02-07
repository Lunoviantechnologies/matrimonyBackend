package com.example.matrimony.referral;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class ReferralService {

    private static final long REQUIRED_FOR_REWARD = 2;
    private static final BigDecimal REWARD_AMOUNT = BigDecimal.valueOf(100);
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final ReferralRepository referralRepository;
    private final ProfileRepository profileRepository;
    private final SecureRandom random = new SecureRandom();

    public ReferralService(ReferralRepository referralRepository,
                           ProfileRepository profileRepository) {
        this.referralRepository = referralRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public String getOrCreateReferralCode(Profile profile) {
        if (profile.getReferralCode() != null && !profile.getReferralCode().isBlank()) {
            return profile.getReferralCode();
        }
        String code;
        do {
            code = generateCode(8);
        } while (profileRepository.existsByReferralCode(code));

        profile.setReferralCode(code);
        profileRepository.save(profile);
        return code;
    }

    private String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(CODE_CHARS.length());
            sb.append(CODE_CHARS.charAt(idx));
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }

    @Transactional
    public Referral applyReferralCode(Profile referredUser, String referralCode) {
        if (referralCode == null || referralCode.isBlank()) {
            throw new IllegalArgumentException("Referral code is required");
        }

        Profile referrer = profileRepository.findByReferralCode(referralCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));

        if (referrer.getId().equals(referredUser.getId())) {
            throw new IllegalArgumentException("You cannot use your own referral code");
        }

        String referredEmail = referredUser.getEmailId() != null
                ? referredUser.getEmailId().toLowerCase(Locale.ROOT)
                : "";

        if (referralRepository.existsByReferrerAndReferredEmail(referrer, referredEmail)) {
            throw new IllegalStateException("Referral already applied for this email");
        }

        Referral referral = new Referral();
        referral.setReferrer(referrer);
        referral.setReferred(referredUser);
        referral.setReferredEmail(referredEmail);
        referral.setStatus(ReferralStatus.COMPLETED);
        referral.setCompletedAt(LocalDateTime.now());

        referralRepository.save(referral);

        applyRewardIfEligible(referrer);

        return referral;
    }

    @Transactional
    public void markReferralCompletedForUser(Profile referredUser) {
        Referral referral = referralRepository.findByReferred(referredUser)
                .orElseThrow(() -> new IllegalStateException("No referral found for this user"));

        if (referral.getStatus() == ReferralStatus.COMPLETED
                || referral.getStatus() == ReferralStatus.REWARDED) {
            return;
        }

        referral.setStatus(ReferralStatus.COMPLETED);
        referral.setCompletedAt(LocalDateTime.now());
        referralRepository.save(referral);

        applyRewardIfEligible(referral.getReferrer());
    }

    @Transactional
    protected void applyRewardIfEligible(Profile referrer) {
        long completed = referralRepository.countByReferrerAndStatus(referrer, ReferralStatus.COMPLETED);

        // User can earn reward only once (for the first REQUIRED_FOR_REWARD referrals)
        long cappedCompleted = Math.min(completed, REQUIRED_FOR_REWARD);
        long rewardUnits = cappedCompleted / REQUIRED_FOR_REWARD; // 0 or 1 only
        BigDecimal shouldHave = REWARD_AMOUNT.multiply(BigDecimal.valueOf(rewardUnits));

        BigDecimal current = referrer.getReferralRewardBalance() != null
                ? referrer.getReferralRewardBalance()
                : BigDecimal.ZERO;

        if (shouldHave.compareTo(current) > 0) {
            referrer.setReferralRewardBalance(shouldHave);
            profileRepository.save(referrer);
        }
    }

    @Transactional
    public ReferralSummaryDto getSummary(Profile profile, String appBaseUrl) {
        String code = getOrCreateReferralCode(profile);

        long completed = referralRepository.countByReferrerAndStatus(profile, ReferralStatus.COMPLETED);
        long pending = referralRepository.countByReferrerAndStatus(profile, ReferralStatus.PENDING);

        ReferralSummaryDto dto = new ReferralSummaryDto();
        dto.setReferralCode(code);
        dto.setReferralLink(appBaseUrl + "/referral/" + code);
        dto.setCompletedReferrals(completed);
        dto.setPendingReferrals(pending);
        dto.setTotalReferralsNeeded(REQUIRED_FOR_REWARD);
        dto.setRewardBalance(profile.getReferralRewardBalance());
        dto.setEligibleForReward(completed >= REQUIRED_FOR_REWARD);
        dto.setRemainingForNextReward(
                completed >= REQUIRED_FOR_REWARD ? 0 : (REQUIRED_FOR_REWARD - completed)
        );
        return dto;
    }
}

