package com.example.matrimony.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/bugs")
public class AdminBugReportController {

    private final AdminBugReportService service;

    public AdminBugReportController(AdminBugReportService service) {
        this.service = service;
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportBug(
            @Validated @RequestBody AdminBugReportRequest request) {

        AdminBugReport bug = service.reportBug(request);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "bugId", bug.getId(),
                        "message", "Bug reported successfully"
                )
        );
    }
}