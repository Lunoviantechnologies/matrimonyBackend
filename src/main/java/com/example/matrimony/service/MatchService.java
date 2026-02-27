package com.example.matrimony.service;

import com.example.matrimony.dto.ProfileFilterRequest;
import com.example.matrimony.dto.ProfileMatchResponse;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.specification.ProfileSpecification;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final ProfileRepository profileRepository;
    private final ProfilePhotoService profilePhotoService;

    public MatchService(ProfileRepository profileRepository,
                        ProfilePhotoService profilePhotoService) {
        this.profileRepository = profileRepository;
        this.profilePhotoService = profilePhotoService;
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

        List<Long> ids = page.getContent()
                .stream()
                .map(Profile::getId)
                .collect(Collectors.toList());

        Map<Long, Map<Integer, String>> photoMap =
                profilePhotoService.getPhotosForProfiles(ids);

        return page.map(p -> {

            Map<Integer, String> userPhotos =
                    photoMap.getOrDefault(p.getId(), Collections.emptyMap());

            return new ProfileMatchResponse(
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
                    userPhotos.get(0),
                    userPhotos.get(1),
                    userPhotos.get(2),
                    userPhotos.get(3),
                    userPhotos.get(4),
                    p.getHideProfilePhoto(),
                    p.getGender()
            );
        });
}
}