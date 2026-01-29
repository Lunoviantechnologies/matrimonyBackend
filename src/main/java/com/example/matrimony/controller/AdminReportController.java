package com.example.matrimony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.dto.BanRequest;
import com.example.matrimony.entity.UserReport;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.repository.DeletedProfileRepository;

import com.example.matrimony.service.AdminReportService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    @Autowired
    private AdminReportService adminReportService;
    @Autowired
    private DeletedProfileRepository deletedProfileRepo;

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
    
    @PutMapping("/banuser/{userId}")
    public ResponseEntity<String> banUser(@PathVariable Long userId,
                                          @RequestBody BanRequest request) {
        adminReportService.banUser(userId, request.getReason());
        return ResponseEntity.ok("User banned successfully");
    }
    
    //Get All Api
    @GetMapping("/reports/GetAll")
    public ResponseEntity<List<UserReport>> getAllReports() {
        return ResponseEntity.ok(adminReportService.getPendingReports());
    }

}

//
//    @GetMapping("/GetAll")
//    public ResponseEntity<?> getAllReports() {
//        return ResponseEntity.ok(adminReportService.getAllReports());
//    }
//}

