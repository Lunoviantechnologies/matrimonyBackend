package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.DashboardSummaryDto;
import com.example.matrimony.dto.ProfileCardDto;
import com.example.matrimony.dto.SubscriptionPlanResponse;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.referral.ReferralService;
import com.example.matrimony.referral.ReferralSummaryDto;
import com.example.matrimony.repository.FriendRequestRepository;
import com.example.matrimony.repository.PaymentRecordRepository;
import com.example.matrimony.repository.ProfileRepository;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ProfileRepository profileRepo;
    private final FriendRequestRepository friendRepo;
    private final ReferralService referralService;
    private final PaymentRecordRepository paymentRepo;

    public DashboardService(ProfileRepository profileRepo,
                            FriendRequestRepository friendRepo,
                            PaymentRecordRepository paymentRepo,
                            ReferralService referralService) {
        this.profileRepo = profileRepo;
        this.friendRepo = friendRepo;
        this.referralService = referralService;
        this.paymentRepo = paymentRepo;
    }

    // ================= MAIN DASHBOARD =================
    public DashboardSummaryDto getDashboardByEmail(String email) {

        Profile me = profileRepo.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildDashboard(me);
    }

    private DashboardSummaryDto buildDashboard(Profile me) {

        Long userId = me.getId();

        String myGender = me.getGender() != null
                ? me.getGender().toLowerCase()
                : "";

        String opposite = "male".equals(myGender) ? "female" : "male";

        // ---------- hidden ids ----------
        List<Long> hiddenIds = friendRepo.findHiddenProfileIds(userId);
        if (hiddenIds == null || hiddenIds.isEmpty()) {
            hiddenIds = List.of(-1L);
        }

        // ---------- NEW MATCHES ----------
        LocalDateTime fourDaysAgo = LocalDateTime.now().minusDays(4);

        List<Profile> newMatches =
                profileRepo.findDashboardProfiles(opposite, userId, hiddenIds, fourDaysAgo);

        // ---------- PREMIUM MATCHES ----------
        List<Profile> premiumMatches =
                profileRepo.findPremiumDashboardProfiles(opposite, userId, hiddenIds);

        // ---------- RECOMMENDED ----------
        List<Profile> recommended =
                profileRepo.findRecommendedProfiles(
                        opposite,
                        userId,
                        hiddenIds,
                        PageRequest.of(0, 10)
                ).getContent();

        // ---------- COUNTS ----------
        long received = friendRepo.countReceived(userId);
        long accepted = friendRepo.countAccepted(userId);
        long rejected = friendRepo.countRejected(userId);
        long sent = friendRepo.countSent(userId);

        // ---------- REFERRAL ----------
        ReferralSummaryDto referralSummary =
                referralService.getSummary(me, "https://vivahjeevan.com");

        // ---------- SUBSCRIPTION ----------
        SubscriptionPlanResponse sub = null;

        Optional<PaymentRecord> paymentOpt =
                paymentRepo.findActiveSubscription(userId);

        if (paymentOpt.isPresent()) {
            PaymentRecord pay = paymentOpt.get();

            sub = new SubscriptionPlanResponse();
            sub.setPlanCode(pay.getPlanCode());
            sub.setPlanName(pay.getPlanName());
            sub.setPremiumStart(pay.getPremiumStart());
            sub.setPremiumEnd(pay.getPremiumEnd());
            sub.setExpiryMessage(pay.getExpiryMessage());
            sub.setAmount(pay.getAmount());
        }

        // ---------- BUILD DTO ----------
        DashboardSummaryDto dto = new DashboardSummaryDto();

        dto.setNewMatches(mapProfiles(newMatches));
        dto.setPremiumMatches(mapProfiles(premiumMatches));
        dto.setRecommendedMatches(mapProfiles(recommended));

        dto.setReceivedCount(received);
        dto.setAcceptedCount(accepted);
        dto.setRejectedCount(rejected);
        dto.setSentCount(sent);

        dto.setReferral(referralSummary);
        dto.setSubscription(sub);

        return dto;
    }

    // ================= PROFILE MAPPER =================
    private List<ProfileCardDto> mapProfiles(List<Profile> list) {

        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream().map(p -> {

            ProfileCardDto d = new ProfileCardDto();

            d.setId(p.getId());

            String fullName =
                    (p.getFirstName() != null ? p.getFirstName() : "") + " " +
                    (p.getLastName() != null ? p.getLastName() : "");

            d.setName(fullName.trim());
            d.setAge(p.getAge());
            d.setCity(p.getCity());
            d.setGender(p.getGender());
            d.setHideProfilePhoto(p.getHideProfilePhoto());
            d.setProfession(p.getOccupation());
            d.setUpdatePhoto(p.getUpdatePhoto());
            d.setMotherTongue(p.getMotherTongue());
            d.setCountry(p.getCountry());

            // premium flag
            d.setPremium(
                    p.getPremiumEnd() != null &&
                    p.getPremiumEnd().isAfter(LocalDateTime.now())
            );

            return d;

        }).toList();
    }
}