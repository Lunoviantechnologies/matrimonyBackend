package com.example.matrimony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.ReportReason;
import com.example.matrimony.entity.UserReport;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.repository.UserReportRepository;

@Service
public class UserReportService {

    @Autowired
    private UserReportRepository reportRepo;

    @Autowired
    private ProfileRepository profileRepo;

    public UserReport reportUser(
            Long reporterId,
            Long reportedUserId,
            ReportReason reason,
            String description) {

        if (reporterId.equals(reportedUserId)) {
            throw new RuntimeException("You cannot report yourself");
        }

        Profile reporter = profileRepo.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        Profile reportedUser = profileRepo.findById(reportedUserId)
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        UserReport report = new UserReport();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(reason);
        report.setDescription(description);

        return reportRepo.save(report);
    }
}