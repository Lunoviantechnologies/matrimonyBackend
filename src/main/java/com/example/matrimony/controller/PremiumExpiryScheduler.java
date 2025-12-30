package com.example.matrimony.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;

public class PremiumExpiryScheduler {

	 private final ProfileRepository profileRepo;

	    public PremiumExpiryScheduler(ProfileRepository profileRepo) {
	        this.profileRepo = profileRepo;
	    }

	    // Runs every 30 seconds
	    @Scheduled(fixedRate = 30000)
	    @Transactional
	    public void expirePremiums() {

	        List<Profile> expiredProfiles =
	                profileRepo.findByPremiumTrueAndPremiumEndBefore(LocalDateTime.now());

	        for (Profile profile : expiredProfiles) {
	            profile.setPremium(false);
	            profile.setPremiumEnd(null);
	            profile.setPremiumStart(null);
	        }

	        profileRepo.saveAll(expiredProfiles);
	    }
}
