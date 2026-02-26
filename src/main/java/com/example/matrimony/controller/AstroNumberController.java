package com.example.matrimony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.entity.AstroNumber;
import com.example.matrimony.service.AstroNumberService;
@RestController
@RequestMapping("/api/astro-number")
public class AstroNumberController {

    @Autowired
    private AstroNumberService service;

    //  User access (Platinum only)
    @GetMapping("/all/{profileId}")
    public List<AstroNumber> getAll(@PathVariable Long profileId) {
        return service.getAll(profileId);
    }

    //  Admin access
    @GetMapping("/admin/all")
    public List<AstroNumber> getAdminAll() {
        return service.getAllForAdmin();
    }

    @PostMapping("/add")
    public AstroNumber add(@RequestBody AstroNumber astroNumber) {
        return service.save(astroNumber);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAstroNumber(
            @PathVariable Long id,
            @RequestBody AstroNumber astroNumber) {

        AstroNumber updated = service.update(id, astroNumber);
        return ResponseEntity.ok(updated);
    }
}


