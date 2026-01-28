package com.example.matrimony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.entity.UserReport;
import com.example.matrimony.service.AdminReportService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    @Autowired
    private AdminReportService adminReportService;

    // ================= GET PENDING REPORTS =================
    
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingReports() {
        return ResponseEntity.ok(adminReportService.getPendingReports());
    }

    // ================= APPROVE REPORT =================
    @PostMapping("/{reportId}/approve")
    public ResponseEntity<?> approveReport(
            @PathVariable Long reportId,
            @RequestBody(required = false) String adminComment) {

        adminReportService.approveReport(reportId, adminComment);
        return ResponseEntity.ok("Report approved. User banned.");
    }

    // ================= REJECT REPORT =================
//    @PostMapping("/{reportId}/reject")
//    public ResponseEntity<?> rejectReport(
//            @PathVariable Long reportId,
//            @RequestBody(required = false) String adminComment) {
//
//        adminReportService.rejectReport(reportId, adminComment);
//        return ResponseEntity.ok("Report rejected.");
//    }
    
    
    
  //PUT method for update the status
    @PutMapping("/reports/{reportId}/reject")
    public ResponseEntity<?> rejectReport(
            @PathVariable Long reportId,
            @RequestBody(required = false) String adminComment) {

        adminReportService.rejectReport(reportId, adminComment);
        return ResponseEntity.ok("Report rejected.");
    }

    // ================= SUPER ADMIN HARD DELETE =================
    @DeleteMapping("/backup-delete/{userId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> permanentDeleteUser(@PathVariable Long userId) {

        adminReportService.permanentDeleteUser(userId);
        return ResponseEntity.ok("User permanently deleted.");
    }
    
    //Get All Api
    @GetMapping("/reports/GetAll")
    public ResponseEntity<List<UserReport>> getAllReports() {
        return ResponseEntity.ok(adminReportService.getPendingReports());
    }
}