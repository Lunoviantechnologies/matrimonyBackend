package com.example.matrimony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.service.AdminReportService;
@RestController
@RequestMapping("/api/admin")
public class AdminReportController {

    @Autowired
    private AdminReportService adminReportService;

    @DeleteMapping("/delete-profile/{reportId}")
    public ResponseEntity<?> deleteReportedProfile(@PathVariable Long reportId) {

        adminReportService.deleteReportedProfileByReportId(reportId);

        return ResponseEntity.ok("Profile deleted and chat archived successfully");
    }
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportById(@PathVariable Long reportId) {
        return ResponseEntity.ok(adminReportService.getReportById(reportId));
    }
    
    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserReport(@PathVariable Long id) {
    	adminReportService.deleteReportById(id);
        return ResponseEntity.ok("User report deleted successfully");
    }

    @GetMapping("/GetAll")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(adminReportService.getAllReports());
    }
}
