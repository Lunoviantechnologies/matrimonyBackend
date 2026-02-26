package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.matrimony.entity.AstroNumber;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.repository.AstroNumberRepository;
import com.example.matrimony.repository.PaymentRecordRepository;
@Service
public class AstroNumberService {

    @Autowired
    private AstroNumberRepository repository;

    @Autowired
    private PaymentRecordRepository paymentRepo;

    // ================= USER ACCESS (PLATINUM ONLY) =================
    public List<AstroNumber> getAll(Long profileId) {

        List<PaymentRecord> payments = paymentRepo.findByProfileId(profileId);

        PaymentRecord latest = payments.stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .max(Comparator.comparing(PaymentRecord::getPremiumEnd))
                .orElse(null);

        if (latest == null ||
            latest.getPremiumEnd() == null ||
            latest.getPremiumEnd().isBefore(LocalDateTime.now()) ||
            latest.getPlanCode() == null ||
            !latest.getPlanCode().toUpperCase().contains("PLATINUM")) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Astrologers available only for Platinum members"
            );
        }

        return repository.findAll();
    }

    // ================= ADMIN ACCESS (NO RESTRICTION) =================
    public List<AstroNumber> getAllForAdmin() {
        return repository.findAll();
    }

    public AstroNumber save(AstroNumber astroNumber) {
        return repository.save(astroNumber);
    }

    public AstroNumber update(Long id, AstroNumber astroNumber) {
        AstroNumber existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AstroNumber not found"));

        if (astroNumber.getAstroNumber() != null)
            existing.setAstroNumber(astroNumber.getAstroNumber());

        if (astroNumber.getName() != null)
            existing.setName(astroNumber.getName());

        if (astroNumber.getLanguages() != null)
            existing.setLanguages(astroNumber.getLanguages());

        if (astroNumber.getExperience() != null)
            existing.setExperience(astroNumber.getExperience());

        if (astroNumber.getPrice() != null)
            existing.setPrice(astroNumber.getPrice());

        return repository.save(existing);
    }
}