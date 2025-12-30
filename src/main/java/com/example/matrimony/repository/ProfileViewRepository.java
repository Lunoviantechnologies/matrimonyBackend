package com.example.matrimony.repository;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.ProfileView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProfileViewRepository extends JpaRepository<ProfileView, Long> {

    Optional<ProfileView> findByProfileAndViewDate(Profile profile, LocalDate viewDate);
    List<ProfileView> findAllByProfile(Profile profile);
}
