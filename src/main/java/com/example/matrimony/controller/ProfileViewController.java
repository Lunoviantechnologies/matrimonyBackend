package com.example.matrimony.controller;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.service.ProfileViewService;
import com.example.matrimony.repository.ProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileViewController {

    private final ProfileViewService profileViewService;
    private final ProfileRepository profileRepository;

    public ProfileViewController(ProfileViewService profileViewService,
                                 ProfileRepository profileRepository) {
        this.profileViewService = profileViewService;
        this.profileRepository = profileRepository;
    }

    /**
     * Record a profile view
     * Example: POST /api/profiles/record/2/1
     * Means: user with ID 1 viewed profile with ID 2
     */
    @PostMapping("/record/{viewerId}/{profileId}")
    public String viewProfile(@PathVariable Long profileId, @PathVariable Long viewerId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        Optional<Profile> viewerOpt = profileRepository.findById(viewerId);

        if (profileOpt.isEmpty() || viewerOpt.isEmpty()) {
            return "Profile or Viewer not found!";
        }

        Profile profile = profileOpt.get();       // Profile being viewed
        Profile viewerProfile = viewerOpt.get();  // Profile who viewed (optional for logging)

        // Record the view (increments the viewed profile's count)
        profileViewService.recordProfileView(profile, viewerProfile);

        int totalViewsToday = profileViewService.getViewCount(profile);
        return "Profile viewed. Total views today: " + totalViewsToday;
    }

    /**
     * Get today's view count for a profile
     */
    @GetMapping("/views/{profileId}")
    public String getProfileViewCount(@PathVariable Long profileId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);

        if (profileOpt.isEmpty()) {
            return "Profile not found!";
        }

        Profile profile = profileOpt.get();
        int totalViewsToday = profileViewService.getViewCount(profile);
        return "Total views today: " + totalViewsToday;
    }

    /**
     * Get total views across all days for a profile
     */
    @GetMapping("/total-views/{profileId}")
    public String getTotalProfileViews(@PathVariable Long profileId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);

        if (profileOpt.isEmpty()) {
            return "Profile not found!";
        }

        Profile profile = profileOpt.get();
        int totalViews = profileViewService.getTotalViewCount(profile);
        return "Total views: " + totalViews;
    }
}
