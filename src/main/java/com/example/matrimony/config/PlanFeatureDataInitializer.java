package com.example.matrimony.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.matrimony.entity.SubscriptionPlan;
import com.example.matrimony.repository.PlanFeatureRepository;
import com.example.matrimony.repository.SubscriptionPlanRepository;
import com.example.util.PlanFeatureMasterData;

@Component
public class PlanFeatureDataInitializer implements CommandLineRunner {

    @Autowired
    private PlanFeatureRepository featureRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Override
    public void run(String... args) {

        // If already inserted, skip
        if (featureRepository.count() == 5) {
            System.out.println("✅ Plan Features already loaded");
            return;
        }

        // Fetch plans
        SubscriptionPlan p1 = planRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan 1 not found"));

        SubscriptionPlan p2 = planRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan 2 not found"));

        SubscriptionPlan p3 = planRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan 3 not found"));

        SubscriptionPlan p4 = planRepository.findById(4L)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan 4 not found"));

        SubscriptionPlan p5 = planRepository.findById(5L)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan 5 not found"));

        // Insert features
        featureRepository.deleteAll();
        featureRepository.saveAll(PlanFeatureMasterData.getAll(p1, p2, p3, p4, p5));

        System.out.println("✅ Plan Features inserted successfully");
    }
}
