package com.example.matrimony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.entity.ReportReason;
import com.example.matrimony.service.UserReportService;

@RestController
@RequestMapping("/api/reports")
public class UserReportController {

    @Autowired
    private UserReportService reportService;

    @PostMapping("/user/{reportedUserId}")
    public ResponseEntity<?> reportUser(
            @PathVariable Long reportedUserId,
            @RequestParam Long reporterId,
            @RequestParam ReportReason reason,
            @RequestBody(required = false) String description) {

        return ResponseEntity.ok(
                reportService.reportUser(
                        reporterId,
                        reportedUserId,
                        reason,
                        description
                )
        );
    }
}