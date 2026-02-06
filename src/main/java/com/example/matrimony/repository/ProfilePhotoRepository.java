package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.Profilepicture;

public interface ProfilePhotoRepository extends JpaRepository<Profilepicture, Long> {

    List<Profilepicture> findByProfile_Id(Long profileId);

    Optional<Profilepicture> findByProfile_IdAndPhotoNumber(Long profileId, Integer photoNumber);

    void deleteByProfile_IdAndPhotoNumber(Long profileId, Integer photoNumber);
}
