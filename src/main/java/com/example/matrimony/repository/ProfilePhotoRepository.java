package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.matrimony.entity.Profilepicture;

import jakarta.transaction.Transactional;

public interface ProfilePhotoRepository extends JpaRepository<Profilepicture, Long> {

    List<Profilepicture> findByProfile_Id(Long profileId);

    Optional<Profilepicture> findByProfile_IdAndPhotoNumber(Long profileId, Integer photoNumber);

    void deleteByProfile_IdAndPhotoNumber(Long profileId, Integer photoNumber);
    
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Profilepicture p WHERE p.profile.id = :id")
    void deleteByProfileId(@Param("id") Long id);

}
