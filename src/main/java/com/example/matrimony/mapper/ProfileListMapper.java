package com.example.matrimony.mapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.matrimony.dto.PaymentSummaryDto;
import com.example.matrimony.dto.ProfileListDto;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;

public class ProfileListMapper {

    public static ProfileListDto toDto(Profile p) {
        if (p == null) return null;

        ProfileListDto dto = new ProfileListDto();

        dto.setId(p.getId());
        dto.setProfileFor(p.getProfileFor());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setMobileNumber(p.getMobileNumber());
        dto.setRole(p.getRole());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setAge(p.getAge() > 0 ? p.getAge() : null);
        dto.setEmailId(p.getEmailId());
        dto.setGender(p.getGender());

        dto.setCity(p.getCity());
        dto.setState(p.getState());
        dto.setCountry(p.getCountry());

        dto.setOccupation(p.getOccupation());
        dto.setCompanyName(p.getCompanyName());
        dto.setHighestEducation(p.getHighestEducation());
        dto.setAnnualIncome(p.getAnnualIncome());

        dto.setMembershipType(p.getMembershipType());
        dto.setAccountStatus(p.getAccountStatus());
        dto.setPremium(p.isPremium());
        dto.setActive(p.getActive());
        dto.setActiveFlag(Boolean.TRUE.equals(p.getActive()));
        dto.setLastActive(p.getLastActive());
        dto.setCreatedAt(p.getCreatedAt());

        // ===============================
        // GET ONLY LATEST PAID PAYMENT
        // ===============================
        PaymentSummaryDto latestPayment = mapLatestPaidPayment(p);
        dto.setLatestPayment(latestPayment);

        // optional: if you still want all payments history
        List<PaymentSummaryDto> allPayments =
                p.getPayments() == null ? Collections.emptyList() :
                p.getPayments().stream()
                        .map(ProfileListMapper::mapPayment)
                        .collect(Collectors.toList());

        dto.setPayments(allPayments);

        return dto;
    }

    // =====================================================
    // LATEST PAID PAYMENT ONLY (IMPORTANT)
    // =====================================================
    private static PaymentSummaryDto mapLatestPaidPayment(Profile p) {

        if (p.getPayments() == null || p.getPayments().isEmpty()) {
            return null;
        }

        PaymentRecord latest = p.getPayments().stream()
                .filter(pay -> "PAID".equals(pay.getStatus()))
                .max(Comparator.comparing(PaymentRecord::getCreatedAt))
                .orElse(null);

        if (latest == null) return null;

        return mapPayment(latest);
    }

    // =====================================================
    // NORMAL PAYMENT MAPPING
    // =====================================================
    private static PaymentSummaryDto mapPayment(PaymentRecord r) {
        if (r == null) return null;

        PaymentSummaryDto dto = new PaymentSummaryDto();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setPlanCode(r.getPlanCode());
        dto.setAmount(r.getAmount());
        dto.setCurrency(r.getCurrency());
        dto.setStatus(r.getStatus());
        dto.setRazorpayOrderId(r.getRazorpayOrderId());
        dto.setRazorpayPaymentId(r.getRazorpayPaymentId());
        dto.setCreatedAt(r.getCreatedAt());

        dto.setPlanName(r.getPlanName());
        dto.setPremiumStart(r.getPremiumStart());
        dto.setPremiumEnd(r.getPremiumEnd());
        dto.setExpiryMessage(r.getExpiryMessage());

        return dto;
    }
}
