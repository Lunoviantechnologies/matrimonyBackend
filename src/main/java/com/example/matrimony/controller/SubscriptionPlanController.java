package com.example.matrimony.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.UpdateSubscriptionPlanFullRequest;
import com.example.matrimony.entity.DiscountType;
import com.example.matrimony.entity.PlanFeature;
import com.example.matrimony.entity.SubscriptionPlan;
import com.example.matrimony.repository.SubscriptionPlanRepository;
import com.example.matrimony.service.PricingService;

@RestController
@RequestMapping("/api")
public class SubscriptionPlanController {

    private final SubscriptionPlanRepository repo;
    private final PricingService pricingService;

    public SubscriptionPlanController(
            SubscriptionPlanRepository repo,
            PricingService pricingService) {
        this.repo = repo;
        this.pricingService = pricingService;
    }

    
    /* ===================== USER ===================== */
    @GetMapping("/plans")
    public ResponseEntity<List<UpdateSubscriptionPlanFullRequest>> getAllPlansForUser() {

        List<SubscriptionPlan> plans = repo.findAll();

        if (plans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UpdateSubscriptionPlanFullRequest> response =
                plans.stream()
                     .map(plan -> {

                         UpdateSubscriptionPlanFullRequest dto =
                                 new UpdateSubscriptionPlanFullRequest();

                         // ===== Plan basic details =====
                        
                         dto.setPlanCode(plan.getPlanCode());
                         dto.setPlanName(plan.getPlanName());
                         dto.setDurationMonths(plan.getDurationMonths());
                         dto.setPriceRupees(plan.getPriceRupees());
                         dto.setId(plan.getId());
                         
                         
                         

                         // ===== Festival pricing =====
                         dto.setFestivalPrice(plan.getFestivalPrice());
                         dto.setFestivalStart(
                                 plan.getFestivalStart() != null
                                         ? plan.getFestivalStart().toLocalDate().toString()
                                         : null
                         );
                         dto.setFestivalEnd(
                                 plan.getFestivalEnd() != null
                                         ? plan.getFestivalEnd().toLocalDate().toString()
                                         : null
                         );

                         // ===== Discount =====
                         dto.setDiscountType(plan.getDiscountType());
                         dto.setDiscountValue(plan.getDiscountValue());
                         dto.setDiscountStart(
                                 plan.getDiscountStart() != null
                                         ? plan.getDiscountStart().toLocalDate().toString()
                                         : null
                         );
                         dto.setDiscountEnd(
                                 plan.getDiscountEnd() != null
                                         ? plan.getDiscountEnd().toLocalDate().toString()
                                         : null
                         );

                         // ===== Status =====
                         dto.setActive(Boolean.TRUE.equals(plan.getActive()));
                         
                      // ===== Plan Features =====
                         if (plan.getPlanFeature() != null) {
                             dto.setContacts(plan.getPlanFeature().getContacts());
                             dto.setChat(plan.getPlanFeature().getChat());
                             dto.setAstroSupport(plan.getPlanFeature().getAstroSupport());
                             dto.setRelationshipManager(plan.getPlanFeature().getRelationshipManager());
                             dto.setBenefit(plan.getPlanFeature().getBenefit());
                         }


                         // ✅ REQUIRED
                         return dto;

                     })
                     .toList();

        return ResponseEntity.ok(response);
    }

    /* ===================== ADMIN ===================== */

    /* ===== Create Plan ===== */
   
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/plan")
    @Transactional
    public ResponseEntity<?> createPlan(
            @RequestBody UpdateSubscriptionPlanFullRequest req) {

        /* ================= BASIC VALIDATION ================= */
        if (req.getPlanCode() == null || req.getPlanCode().isBlank()) {
            return ResponseEntity.badRequest().body("Plan code is required");
        }

        if (repo.existsByPlanCode(req.getPlanCode())) {
            return ResponseEntity.badRequest().body("Plan already exists");
        }

        if (req.getPlanName() == null || req.getPlanName().isBlank()) {
            return ResponseEntity.badRequest().body("Plan name is required");
        }

        if (req.getDurationMonths() == null || req.getDurationMonths() <= 0) {
            return ResponseEntity.badRequest().body("Duration months must be > 0");
        }

        if (req.getPriceRupees() == null || req.getPriceRupees() <= 0) {
            return ResponseEntity.badRequest().body("Price must be > 0");
        }

        SubscriptionPlan plan = new SubscriptionPlan();

        /* ================= PLAN DETAILS ================= */
        plan.setPlanCode(req.getPlanCode());
        plan.setPlanName(req.getPlanName());
        plan.setDurationMonths(req.getDurationMonths());
        plan.setPriceRupees(req.getPriceRupees());
        plan.setActive(req.isActive()); // ← IMPORTANT
        
        
        

        /* ================= FESTIVAL PRICING ================= */
        if (req.getFestivalPrice() != null) {

            if (req.getFestivalPrice() <= 0) {
                return ResponseEntity.badRequest()
                        .body("Festival price must be greater than 0");
            }

            if (req.getFestivalStart() == null || req.getFestivalEnd() == null) {
                return ResponseEntity.badRequest()
                        .body("Festival start and end dates are required");
            }

            LocalDateTime fs;
            LocalDateTime fe;
            try {
                fs = LocalDateTime.parse(req.getFestivalStart());
                fe = LocalDateTime.parse(req.getFestivalEnd());
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Invalid festival date format (yyyy-MM-ddTHH:mm:ss)");
            }

            if (fs.isAfter(fe)) {
                return ResponseEntity.badRequest()
                        .body("Festival start must be before festival end");
            }

            plan.setFestivalPrice(req.getFestivalPrice());
            plan.setFestivalStart(fs);
            plan.setFestivalEnd(fe);
        }

        /* ================= DISCOUNT ================= */
        boolean discountPresent =
                req.getDiscountType() != null ||
                req.getDiscountValue() != null ||
                req.getDiscountStart() != null ||
                req.getDiscountEnd() != null;

        if (discountPresent) {

            if (req.getDiscountType() == null ||
                req.getDiscountValue() == null ||
                req.getDiscountStart() == null ||
                req.getDiscountEnd() == null) {

                return ResponseEntity.badRequest()
                        .body("All discount fields are required");
            }

            if (req.getDiscountType() == DiscountType.PERCENTAGE &&
                (req.getDiscountValue() < 0 || req.getDiscountValue() > 100)) {

                return ResponseEntity.badRequest()
                        .body("Percentage discount must be between 0 and 100");
            }

            LocalDateTime ds;
            LocalDateTime de;
            try {
                ds = LocalDateTime.parse(req.getDiscountStart());
                de = LocalDateTime.parse(req.getDiscountEnd());
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Invalid discount date format (yyyy-MM-ddTHH:mm:ss)");
            }

            if (ds.isAfter(de)) {
                return ResponseEntity.badRequest()
                        .body("Discount start must be before discount end");
            }

            plan.setDiscountType(req.getDiscountType());
            plan.setDiscountValue(req.getDiscountValue());
            plan.setDiscountStart(ds);
            plan.setDiscountEnd(de);
        }

        repo.save(plan);
        return ResponseEntity.ok("Subscription plan created successfully");
    }


    //===========
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/plan/{id}")
    @Transactional
    public ResponseEntity<?> updateSubscriptionPlan(
            @PathVariable Long id,
            @RequestBody UpdateSubscriptionPlanFullRequest req) {

        SubscriptionPlan plan = repo.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found"));

        /* ================= PLAN DETAILS ================= */
        if (req.getPlanCode() != null && !req.getPlanCode().isBlank()) {
            plan.setPlanCode(req.getPlanCode());
        }

        if (req.getPlanName() != null && !req.getPlanName().isBlank()) {
            plan.setPlanName(req.getPlanName());
        }

        if (req.getDurationMonths() != null && req.getDurationMonths() > 0) {
            plan.setDurationMonths(req.getDurationMonths());
        }

        if (req.getPriceRupees() != null && req.getPriceRupees() > 0) {
            plan.setPriceRupees(req.getPriceRupees());
        }

        /* ================= FESTIVAL PRICING ================= */
        boolean festivalTouched =
                req.getFestivalPrice() != null ||
                req.getFestivalStart() != null ||
                req.getFestivalEnd() != null;

        if (festivalTouched) {

            if (req.getFestivalPrice() == null) {
                plan.setFestivalPrice(null);
                plan.setFestivalStart(null);
                plan.setFestivalEnd(null);
            } else { 

                if (req.getFestivalPrice() <= 0) {
                    return ResponseEntity.badRequest()
                            .body("Festival price must be greater than 0");
                }

                if (req.getFestivalStart() == null || req.getFestivalEnd() == null) {
                    return ResponseEntity.badRequest()
                            .body("Festival start and end dates are required");
                }

                LocalDateTime fs;
                LocalDateTime fe;
                try {
                    fs = LocalDateTime.parse(req.getFestivalStart());
                    fe = LocalDateTime.parse(req.getFestivalEnd());
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body("Invalid festival date format (yyyy-MM-ddTHH:mm:ss)");
                }

                if (fs.isAfter(fe)) {
                    return ResponseEntity.badRequest()
                            .body("Festival start must be before festival end");
                }

                plan.setFestivalPrice(req.getFestivalPrice());
                plan.setFestivalStart(fs);
                plan.setFestivalEnd(fe);
            }
        }

        /* ================= DISCOUNT ================= */
        boolean discountTouched =
                req.getDiscountType() != null ||
                req.getDiscountValue() != null ||
                req.getDiscountStart() != null ||
                req.getDiscountEnd() != null;

        if (discountTouched) {

            if (req.getDiscountType() == null ||
                req.getDiscountValue() == null ||
                req.getDiscountStart() == null ||
                req.getDiscountEnd() == null) {

                return ResponseEntity.badRequest()
                        .body("All discount fields are required when updating discount");
            }

            if (req.getDiscountType() == DiscountType.PERCENTAGE &&
                (req.getDiscountValue() < 0 || req.getDiscountValue() > 100)) {

                return ResponseEntity.badRequest()
                        .body("Percentage discount must be between 0 and 100");
            }

            LocalDateTime ds;
            LocalDateTime de;
            try {
                ds = LocalDateTime.parse(req.getDiscountStart());
                de = LocalDateTime.parse(req.getDiscountEnd());
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Invalid discount date format (yyyy-MM-ddTHH:mm:ss)");
            }

            if (ds.isAfter(de)) {
                return ResponseEntity.badRequest()
                        .body("Discount start must be before discount end");
            }

            plan.setDiscountType(req.getDiscountType());
            plan.setDiscountValue(req.getDiscountValue());
            plan.setDiscountStart(ds);
            plan.setDiscountEnd(de);
        }

        repo.save(plan);
        return ResponseEntity.ok("Subscription plan updated successfully");
    }

    
   
    @GetMapping("/admin/plans/all")
    public ResponseEntity<List<SubscriptionPlan>> getAllPlansForAdmin() {

        List<SubscriptionPlan> plans = repo.findAll();

        if (plans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(plans);
    }

}