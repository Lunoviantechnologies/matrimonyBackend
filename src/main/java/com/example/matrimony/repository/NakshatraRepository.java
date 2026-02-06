package com.example.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.Nakshatra;

public interface NakshatraRepository extends JpaRepository<Nakshatra, Long> {
    Optional<Nakshatra> findBySequence(int sequence);
}
