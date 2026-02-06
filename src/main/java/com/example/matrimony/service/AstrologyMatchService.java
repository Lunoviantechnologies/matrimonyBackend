package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.AstrologyMatch;
import com.example.matrimony.entity.Nakshatra;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.AstrologyMatchRepository;
import com.example.matrimony.repository.ProfileRepository;

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

    public AstrologyMatch match(Long id1, Long id2) {

        Profile p1 = profileRepo.findById(id1).orElseThrow();
        Profile p2 = profileRepo.findById(id2).orElseThrow();

        Nakshatra n1 = nakshatraService.calculateNakshatra(p1.getDateOfBirth());
        Nakshatra n2 = nakshatraService.calculateNakshatra(p2.getDateOfBirth());

        int score = scoreService.calculateScore(n1, n2);

        AstrologyMatch match = new AstrologyMatch();
        match.setProfileOne(p1);
        match.setProfileTwo(p2);
        match.setNakshatraOne(n1.getName());
        match.setNakshatraTwo(n2.getName());
        match.setScore(score);
        match.setMatchedAt(LocalDateTime.now());

        return matchRepo.save(match);
    }

}
