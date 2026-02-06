package com.example.matrimony.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.AstrologyMatch;

public interface AstrologyMatchRepository
extends JpaRepository<AstrologyMatch, Long> {

boolean existsByProfileOne_IdAndProfileTwo_Id(Long p1, Long p2);

// ✅ Get all matches where profile is involved
List<AstrologyMatch> findByProfileOne_IdOrProfileTwo_Id(Long p1, Long p2);
}
