package com.example.matrimony.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.dto.DashboardSummaryDto;
import com.example.matrimony.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDto> getDashboard(Authentication authentication) {

        String email = authentication.getName();

        DashboardSummaryDto dto = dashboardService.getDashboardByEmail(email);

        return ResponseEntity.ok(dto);
    }
}