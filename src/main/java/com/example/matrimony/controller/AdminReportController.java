package com.example.matrimony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
