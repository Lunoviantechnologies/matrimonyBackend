package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.matrimony.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long> {

    Optional<SubscriptionPlan> findByPlanCodeAndActiveTrue(String planCode);
    
    boolean existsByPlanCode(String planCode);
    List<SubscriptionPlan> findByActiveTrue();
    Optional<SubscriptionPlan> findByIdAndActiveTrue(Long id);
    
    //  FETCH ALL PLANS WITH FEATURES
    @Query("SELECT sp FROM SubscriptionPlan sp LEFT JOIN FETCH sp.planFeature")
    List<SubscriptionPlan> findAllWithFeatures();

    //  FETCH SINGLE PLAN WITH FEATURES
    @Query("SELECT sp FROM SubscriptionPlan sp LEFT JOIN FETCH sp.planFeature WHERE sp.id = :id")
    Optional<SubscriptionPlan> findByIdWithFeature(Long id);
    
    Optional<SubscriptionPlan> findByPlanCodeIgnoreCase(String planCode);


    
}