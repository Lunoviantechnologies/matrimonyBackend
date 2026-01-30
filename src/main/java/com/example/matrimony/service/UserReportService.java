package com.example.matrimony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.*;
import com.example.matrimony.repository.*;

@Service
public class UserReportService {

    @Autowired
    private UserReportRepository reportRepo;

    @Autowired
    private ProfileRepository profileRepo;

    // ================= REPORT USER =================
    @Transactional
    public UserReport reportUser(Long reporterId, Long reportedUserId, ReportReason reason, String description) {

        if (reporterId.equals(reportedUserId)) {
            throw new RuntimeException("You cannot report yourself");
        }

        Profile reporter = profileRepo.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        Profile reportedUser = profileRepo.findById(reportedUserId)
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        // ✅ Prevent duplicate reports
        boolean alreadyReported = reportRepo.existsByReporter_IdAndReportedUser_Id(reporterId, reportedUserId);
        if (alreadyReported) {
            throw new RuntimeException("You already reported this user");
        }

        UserReport report = new UserReport();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(reason);
        report.setDescription(description);
        report.setStatus(ReportStatus.PENDING);
        report.setAdminComment(null); // explicitly set null
        report.setReviewedAt(null);

        return reportRepo.save(report);
    }

    // ================= GET ALL REPORTS =================
    public List<UserReport> getAllReports() {
        return reportRepo.findAll();
    }

    // ================= GET REPORTS BY STATUS =================
    public List<UserReport> getReportsByStatus(ReportStatus status) {
        return reportRepo.findByStatus(status);
    }
}
