package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long> {

    Optional<SubscriptionPlan> findByPlanCodeAndActiveTrue(String planCode);
    
    boolean existsByPlanCode(String planCode);
    List<SubscriptionPlan> findByActiveTrue();
    Optional<SubscriptionPlan> findByIdAndActiveTrue(Long id);


}