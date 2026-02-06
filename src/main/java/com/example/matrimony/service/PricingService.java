package com.example.matrimony.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
     * RETURNS RUPEES (BigDecimal)
     */
    public BigDecimal calculateFinalPrice(String planCode) {

        SubscriptionPlan plan = repo.findByPlanCodeAndActiveTrue(planCode)
                .orElseThrow(() -> new RuntimeException("Invalid or inactive plan"));

        return calculateFinalPriceInternal(plan);
    }

    /**
     * SINGLE source of truth for pricing logic
     * ALL calculations in BigDecimal (RUPEES)
     */
    private BigDecimal calculateFinalPriceInternal(SubscriptionPlan plan) {

        LocalDateTime now = LocalDateTime.now();

        /* ===============================
         * 1. Resolve Base Price
         * =============================== */
        BigDecimal basePrice = plan.getPriceRupees(); // BigDecimal

        if (plan.getFestivalPrice() != null &&
            plan.getFestivalStart() != null &&
            plan.getFestivalEnd() != null &&
            now.isAfter(plan.getFestivalStart()) &&
            now.isBefore(plan.getFestivalEnd())) {

            basePrice = plan.getFestivalPrice(); // BigDecimal
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

            // PERCENTAGE DISCOUNT
            if (plan.getDiscountType() == DiscountType.PERCENTAGE) {

                BigDecimal discountPercent = plan.getDiscountValue();

                if (discountPercent.compareTo(BigDecimal.ZERO) < 0 ||
                    discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                    throw new IllegalStateException(
                        "Discount percentage must be between 0 and 100");
                }

                BigDecimal discountAmount = basePrice
                        .multiply(discountPercent)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                return basePrice
                        .subtract(discountAmount)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            // FLAT DISCOUNT
            BigDecimal finalAmount = basePrice.subtract(plan.getDiscountValue());

            return finalAmount.compareTo(BigDecimal.ZERO) < 0
                    ? BigDecimal.ZERO
                    : finalAmount.setScale(2, RoundingMode.HALF_UP);
        }

        return basePrice.setScale(2, RoundingMode.HALF_UP);
    }
}
