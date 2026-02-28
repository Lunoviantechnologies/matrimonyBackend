package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.repository.PaymentRecordRepository;

@Service
public class PlanValidationService {

    @Autowired
    private PaymentRecordRepository paymentRepo;

    //  ONLY PREMIUM USERS CAN USE ASTRO
    public boolean canUseAstro(Long profileId) {

        List<PaymentRecord> payments = paymentRepo.findByProfileId(profileId);

        if (payments == null || payments.isEmpty()) {
            return false;
        }

        // get latest PAID plan
        PaymentRecord latest = payments.stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .max(Comparator.comparing(PaymentRecord::getPremiumEnd))
                .orElse(null);

        if (latest == null) return false;

        // check expiry
        return latest.getPremiumEnd() != null &&
               latest.getPremiumEnd().isAfter(LocalDateTime.now());
    }
}