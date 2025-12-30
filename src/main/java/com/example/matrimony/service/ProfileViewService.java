package com.example.matrimony.service;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.ProfileView;
import com.example.matrimony.entity.ProfileViewLog;
import com.example.matrimony.repository.ProfileViewRepository;
import com.example.matrimony.repository.ProfileViewLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProfileViewService {

    private final ProfileViewRepository profileViewRepository;
    private final ProfileViewLogRepository profileViewLogRepository;

    public ProfileViewService(ProfileViewRepository profileViewRepository,
                              ProfileViewLogRepository profileViewLogRepository) {
        this.profileViewRepository = profileViewRepository;
        this.profileViewLogRepository = profileViewLogRepository;
    }

    @Transactional
    public void recordProfileView(Profile profile, Profile viewerProfile) {
        LocalDate today = LocalDate.now();

        // 1️⃣ Increment total view count for the profile today
        ProfileView profileView = profileViewRepository
                .findByProfileAndViewDate(profile, today)
                .orElseGet(() -> {
                    ProfileView pv = new ProfileView();
                    pv.setProfile(profile);
                    pv.setViewDate(today);
                    pv.setViewCount(0);
                    return pv;
                });

        profileView.setViewCount(profileView.getViewCount() + 1);
        profileViewRepository.save(profileView);

        // 2️⃣ Optionally store who viewed whom
        ProfileViewLog log = new ProfileViewLog();
        log.setProfile(profile);
        log.setViewer(viewerProfile);
        profileViewLogRepository.save(log);
    }

    public int getViewCount(Profile profile) {
        return profileViewRepository.findByProfileAndViewDate(profile, LocalDate.now())
                .map(ProfileView::getViewCount)
                .orElse(0);
    }

    public int getTotalViewCount(Profile profile) {
        List<ProfileView> views = profileViewRepository.findAllByProfile(profile);
        return views.stream().mapToInt(ProfileView::getViewCount).sum();
    }
}
