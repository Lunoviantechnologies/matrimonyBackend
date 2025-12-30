package com.example.matrimony.mapper;

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
        dto.setActive(p.getActive());                 // returns Boolean (nullable)
        dto.setActiveFlag(Boolean.TRUE.equals(p.getActive())); // also set the sample-style flag
        dto.setLastActive(p.getLastActive());
        dto.setCreatedAt(p.getCreatedAt());

        // payments
        List<PaymentSummaryDto> payments = p.getPayments().stream()
                .map(ProfileListMapper::mapPayment)
                .collect(Collectors.toList());
        dto.setPayments(payments);

        return dto;
    }

    private static PaymentSummaryDto mapPayment(PaymentRecord r) {
        if (r == null) return null;
        PaymentSummaryDto dto = new PaymentSummaryDto();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setPlanCode(r.getPlanCode());
        dto.setAmount(r.getAmount()); // keep same units as entity
        dto.setCurrency(r.getCurrency());
        dto.setStatus(r.getStatus());
        dto.setRazorpayOrderId(r.getRazorpayOrderId());
        dto.setRazorpayPaymentId(r.getRazorpayPaymentId());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
