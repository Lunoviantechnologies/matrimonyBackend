package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.matrimony.dto.AstroMatchResponse;
import com.example.matrimony.entity.AstrologyMatch;
import com.example.matrimony.entity.Nakshatra;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.SubscriptionPlan;
import com.example.matrimony.repository.AstrologyMatchRepository;
import com.example.matrimony.repository.PaymentRecordRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.repository.SubscriptionPlanRepository;
@Service
public class AstrologyMatchService {

    @Autowired
    private ProfileRepository profileRepo;

    @Autowired
    private NakshatraService nakshatraService;

    @Autowired
    private AshtaKootaScoreService scoreService;

    @Autowired
    private AstrologyMatchRepository matchRepo;

    @Autowired
    private PaymentRecordRepository paymentRepo;

    @Autowired
    private SubscriptionPlanRepository planRepo;

    public AstroMatchResponse match(Long id1, Long id2) {

        // ================= PLAN VALIDATION =================

        List<PaymentRecord> payments = paymentRepo.findByProfileId(id1);

        PaymentRecord latest = payments.stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .max(Comparator.comparing(PaymentRecord::getPremiumEnd))
                .orElse(null);

        if (latest == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Premium plan required to use astrology matching"
            );
        }

        if (latest.getPremiumEnd() == null ||
                latest.getPremiumEnd().isBefore(LocalDateTime.now())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your premium plan has expired"
            );
        }

        SubscriptionPlan plan = planRepo
                .findByPlanCodeIgnoreCase(latest.getPlanCode())
                .orElse(null);

        if (plan == null || plan.getPlanFeature() == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Plan not eligible for astrology matching"
            );
        }

        String astroSupport = plan.getPlanFeature().getAstroSupport();

        if (astroSupport == null ||
                astroSupport.equalsIgnoreCase("No")) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Astro score available only for eligible premium plans"
            );
        }

        // ================= ASTRO MATCH LOGIC =================

        Profile p1 = profileRepo.findById(id1)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profile1 not found"));

        Profile p2 = profileRepo.findById(id2)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profile2 not found"));

        Nakshatra n1 = nakshatraService.calculateNakshatra(p1.getDateOfBirth());
        Nakshatra n2 = nakshatraService.calculateNakshatra(p2.getDateOfBirth());

        int score = scoreService.calculateScore(n1, n2);

        // Save match history (optional but good practice)
        AstrologyMatch match = new AstrologyMatch();
        match.setProfileOne(p1);
        match.setProfileTwo(p2);
        match.setNakshatraOne(n1.getName());
        match.setNakshatraTwo(n2.getName());
        match.setScore(score);
        match.setMatchedAt(LocalDateTime.now());

        matchRepo.save(match);

        // ================= RETURN DTO =================

        String message = generateMessage(score);

        return new AstroMatchResponse(
                score,
                n1.getName(),
                n2.getName(),
                message
        );
    }

    // Optional: Score interpretation
    private String generateMessage(int score) {
        if (score >= 28) return "Excellent match";
        if (score >= 20) return "Very good compatibility";
        if (score >= 15) return "Average compatibility";
        return "Low compatibility";
    }
}