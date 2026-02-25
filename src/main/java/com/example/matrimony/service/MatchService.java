package com.example.matrimony.service;

import com.example.matrimony.dto.ProfileFilterRequest;
import com.example.matrimony.dto.ProfileMatchResponse;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.specification.ProfileSpecification;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final ProfileRepository profileRepository;
    public MatchService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Page<ProfileMatchResponse> getMatches(ProfileFilterRequest req) {

        Sort sort = Sort.unsorted();

        switch (req.getSortBy() == null ? "" : req.getSortBy()) {
            case "newest":
                sort = Sort.by("createdAt").descending();
                break;
            case "active":
                sort = Sort.by("lastActive").descending();
                break;
            case "premium":
                sort = Sort.by("premium").descending();
                break;
        }

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);

        Page<Profile> page = profileRepository.findAll(
                ProfileSpecification.filter(req),
                pageable
        );

        return page.map(p -> new ProfileMatchResponse(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getHeight(),
                p.getCity(),
                p.getOccupation(),
                p.getHighestEducation(),
                p.getReligion(),
                p.getSubCaste(),
                p.isPremium(),
                p.getUpdatePhoto(),
                p.getUpdatePhoto1(),
                p.getUpdatePhoto2(),
                p.getUpdatePhoto3(),
                p.getUpdatePhoto4(),
                p.getHideProfilePhoto(),
                p.getGender()   // MUST BE LAST
        ));
    }
}