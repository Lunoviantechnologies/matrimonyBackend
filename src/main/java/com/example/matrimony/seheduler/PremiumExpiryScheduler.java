package com.example.matrimony.seheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
   
    @Value("${admin.email:}")
    private String adminEmail;
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
    //@Scheduled(cron = "0 0 9 * * ?")
    @Scheduled(fixedRate = 60000) 
    @Transactional
    public void handlePremiumExpiry() {

        LocalDateTime now = LocalDateTime.now();

        // ===============================
        // 1️ EXPIRE ALREADY EXPIRED PLANS
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

        // ==============================================
        // 2️ SEND 3-DAY EXPIRY REMINDER EMAILS  for user
        // ==============================================
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
        
     // ====================================================
     // 3️ SEND ADMIN DAILY EXPIRY ALERT (3 DAYS) for admin
     // ====================================================
        
        // ====================================================
        // 3️ SEND ADMIN DAILY EXPIRY ALERT (3 DAYS) for admin
        // ====================================================

       try {

           if (adminEmail == null || adminEmail.isBlank()) {
               log.warn("Admin email not configured. Skipping admin alert.");
           }
           else if (!expiringSoon.isEmpty()) {

               StringBuilder adminMail = new StringBuilder();
               adminMail.append("Premium Expiry Alert - Next 3 Days\n\n");
               adminMail.append("Total Users Expiring: ")
                        .append(expiringSoon.size())
                        .append("\n\n");

               adminMail.append("User Details:\n");
               adminMail.append("---------------------------------\n");

               for (Profile p : expiringSoon) {
                   adminMail.append("ID: ").append(p.getId())
                           .append(" | Name: ").append(p.getFirstName())
                           .append(" | Email: ").append(p.getEmailId())
                           .append(" | Expiry: ").append(p.getPremiumEnd())
                           .append("\n");
               }

               emailService.sendSimpleMail(
                       adminEmail,
                       "Premium Expiring in 3 Days - Admin Alert",
                       adminMail.toString()
               );

               log.info("Admin expiry alert sent. Count={}", expiringSoon.size());
           } 
           else {
               log.info("No users expiring in next 3 days. Admin mail skipped.");
           }

       } catch (Exception e) {
           log.error("Failed to send admin expiry alert", e);
       }
    }
}