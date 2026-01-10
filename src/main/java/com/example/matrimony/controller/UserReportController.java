package com.example.matrimony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.entity.UserReport;
import com.example.matrimony.service.UserReportService;

@RestController
@RequestMapping("/api/reports")
public class UserReportController {

    @Autowired
    private UserReportService reportService;

    @PostMapping("/user/{reportedUserId}")
    public ResponseEntity<?> reportUser(
            @PathVariable Long reportedUserId,
            @RequestBody UserReport request) {

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