package com.example.matrimony.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.matrimony.entity.DiscountType;
import com.example.matrimony.entity.SubscriptionPlan;
import com.example.matrimony.repository.SubscriptionPlanRepository;

@Service
public class PricingService {

    private final SubscriptionPlanRepository repo;

    public PricingService(SubscriptionPlanRepository repo) {
        this.repo = repo;
    }

    /**
     * Entry point used by controllers / payment layer
     */
    public long calculateFinalPrice(String planCode) {

        SubscriptionPlan plan = repo.findByPlanCodeAndActiveTrue(planCode)
                .orElseThrow(() -> new RuntimeException("Invalid or inactive plan"));

        return calculateFinalPriceInternal(plan);
    }

    /**
     * SINGLE source of truth for pricing logic
     */
    private long calculateFinalPriceInternal(SubscriptionPlan plan) {

        LocalDateTime now = LocalDateTime.now();

        /* ===============================
         * 1. Resolve Base Price
         * =============================== */
        long basePrice = plan.getPriceRupees();

        if (plan.getFestivalPrice() != null &&
            plan.getFestivalStart() != null &&
            plan.getFestivalEnd() != null &&
            now.isAfter(plan.getFestivalStart()) &&
            now.isBefore(plan.getFestivalEnd())) {

            basePrice = plan.getFestivalPrice();
        }

        /* ===============================
         * 2. Apply Discount (ADMIN ONLY)
         * =============================== */
        if (plan.getDiscountType() != null &&
            plan.getDiscountValue() != null &&
            plan.getDiscountStart() != null &&
            plan.getDiscountEnd() != null &&
            now.isAfter(plan.getDiscountStart()) &&
            now.isBefore(plan.getDiscountEnd())) {

            if (plan.getDiscountType() == DiscountType.PERCENTAGE) {

                if (plan.getDiscountValue() < 0 || plan.getDiscountValue() > 100) {
                    throw new IllegalStateException(
                        "Discount percentage must be between 0 and 100");
                }

                return basePrice - (basePrice * plan.getDiscountValue() / 100);
            }
            // FLAT discount
            return Math.max(0, basePrice - plan.getDiscountValue());
        }

        return basePrice;
    }
}