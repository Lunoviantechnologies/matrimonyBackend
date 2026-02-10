package com.example.matrimony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.PlanFeature;
import com.example.matrimony.repository.PlanFeatureRepository;

@Service
public class PlanFeatureService {

    @Autowired
    private PlanFeatureRepository repository;

    public List<PlanFeature> getAll() {
        return repository.findAll();
    }

    public PlanFeature getByPlanId(Long planId) {
        return repository.findByPlan_Id(planId)
                .orElseThrow(() -> new RuntimeException("PlanFeature not found for planId: " + planId));
    }
}
