package com.example.matrimony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.entity.PlanFeature;
import com.example.matrimony.service.PlanFeatureService;

@RestController
@RequestMapping("/api/plan-features")
@CrossOrigin(origins = "*")
public class PlanFeatureController {

    @Autowired
    private PlanFeatureService service;

    @GetMapping("/all")
    public List<PlanFeature> getAll() {
        return service.getAll();
    }

    @GetMapping("/{planId}")
    public PlanFeature getByPlanId(@PathVariable Long planId) {
        return service.getByPlanId(planId);
    }
}
