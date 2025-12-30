package com.example.matrimony.seheduler;


import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;

import jakarta.transaction.Transactional;

@Component
public class AccountDeletionScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(AccountDeletionScheduler.class);

    private final ProfileRepository profileRepository;

    public AccountDeletionScheduler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Runs once daily at 2 AM
     * Deletes accounts where delete was requested > 30 days ago
     */
    @Scheduled(cron = "0 0 2 * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void permanentlyDeleteAccounts() {

        LocalDateTime cutoff =
                LocalDateTime.now().minusDays(30); //if you Minutes , Hours

        List<Profile> profiles =
                profileRepository
                        .findByDeleteRequestedTrueAndDeleteRequestedAtBefore(cutoff);

        if (profiles.isEmpty()) {
            log.info("No accounts eligible for permanent deletion");
            return;
        }

        for (Profile profile : profiles) {
            log.info("Permanently deleting profile id={}", profile.getId());
            profileRepository.delete(profile); // HARD DELETE
        }
    }
}