package com.example.matrimony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.AstroNumber;
import com.example.matrimony.repository.AstroNumberRepository;

@Service
public class AstroNumberService {

    @Autowired
    private AstroNumberRepository repository;

    // CREATE
    public AstroNumber save(AstroNumber astroNumber) {
        return repository.save(astroNumber);
    }

    // READ
    public List<AstroNumber> getAll() {
        return repository.findAll();
    }

    // PARTIAL UPDATE
    public AstroNumber update(Long id, AstroNumber astroNumber) {
        AstroNumber existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AstroNumber not found with id: " + id));

        if (astroNumber.getAstroNumber() != null) {
            existing.setAstroNumber(astroNumber.getAstroNumber());
        }
        if (astroNumber.getName() != null) {
            existing.setName(astroNumber.getName());
        }
        if (astroNumber.getLanguages() != null) {
            existing.setLanguages(astroNumber.getLanguages());
        }
        if (astroNumber.getExperience() != null) {
            existing.setExperience(astroNumber.getExperience());
        }
        if (astroNumber.getPrice() != null) {
            existing.setPrice(astroNumber.getPrice());
        }

        return repository.save(existing);
    }
}
