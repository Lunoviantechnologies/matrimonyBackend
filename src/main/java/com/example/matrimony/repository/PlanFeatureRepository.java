package com.example.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.PlanFeature;

public interface PlanFeatureRepository extends JpaRepository<PlanFeature, Long> {

    Optional<PlanFeature> findByPlan_Id(Long planId);

    boolean existsByPlan_Id(Long planId);
}
