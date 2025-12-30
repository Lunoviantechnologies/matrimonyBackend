package com.example.matrimony.seheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.EmailService;

import jakarta.transaction.Transactional;

@Component
public class PremiumExpiryScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(PremiumExpiryScheduler.class);

    private final ProfileRepository profileRepo;
    private final EmailService emailService;

    public PremiumExpiryScheduler(ProfileRepository profileRepo,
                                  EmailService emailService) {
        this.profileRepo = profileRepo;
        this.emailService = emailService;
    }

    /**
     * Runs every day at 9 AM
     * - Expires premium plans
     * - Sends expiry emails
     * - Sends 3-day reminder emails
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void handlePremiumExpiry() {

        LocalDateTime now = LocalDateTime.now();

        // ===============================
        // 1️⃣ EXPIRE ALREADY EXPIRED PLANS
        // ===============================
        List<Profile> expiredProfiles =
                profileRepo.findByPremiumTrueAndPremiumEndBefore(now);

        for (Profile profile : expiredProfiles) {
            profile.setPremium(false);
            profileRepo.save(profile);

            try {
                emailService.sendPlanExpiredEmail(
                        profile.getEmailId(),
                        profile.getFirstName(),
                        profile.getPremiumEnd()
                );
            } catch (Exception e) {
                log.error("Failed to send expiry email for user {}", profile.getId(), e);
            }
        }

        // =========================================
        // 2️⃣ SEND 3-DAY EXPIRY REMINDER EMAILS
        // =========================================
        LocalDateTime warningTime = now.plusDays(3);

        List<Profile> expiringSoon =
                profileRepo.findByPremiumTrueAndPremiumEndAfterAndPremiumEndBefore(
                        now,
                        warningTime
                );

        for (Profile profile : expiringSoon) {
            try {
                emailService.sendPlanExpiryReminderEmail(
                        profile.getEmailId(),
                        profile.getFirstName(),
                        profile.getPremiumEnd()
                );
            } catch (Exception e) {
                log.error("Failed to send reminder email for user {}", profile.getId(), e);
            }
        }

        log.info("Premium expiry job completed. Expired={}, Reminders={}",
                expiredProfiles.size(), expiringSoon.size());
    }
}