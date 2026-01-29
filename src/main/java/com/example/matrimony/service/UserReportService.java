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

    @Transactional
    public UserReport reportUser(Long reporterId, Long reportedUserId, ReportReason reason, String description) {

        Profile reporter = profileRepo.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        Profile reportedUser = profileRepo.findById(reportedUserId)
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        UserReport report = new UserReport();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(reason);
        report.setDescription(description);
        report.setStatus(ReportStatus.PENDING);

        return reportRepo.save(report); // 🔥 THIS SAVES DATA
    }

    public List<UserReport> getAllReports() {
        return reportRepo.findAll();
    }
}