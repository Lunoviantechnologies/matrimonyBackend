package com.example.matrimony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.dto.UserReportRequest;
import com.example.matrimony.service.UserReportService;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class UserReportController {

    @Autowired
    private UserReportService reportService;

    @PostMapping("/user/{reportedUserId}")
    public ResponseEntity<?> reportUser(
            @PathVariable Long reportedUserId,
            @RequestBody UserReportRequest request) {

        return ResponseEntity.ok(
                reportService.reportUser(
                        request.getReporterId(),
                        reportedUserId,
                        request.getReason(),
                        request.getDescription()
                )
        );
    }

    @GetMapping("/GetAll")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}
