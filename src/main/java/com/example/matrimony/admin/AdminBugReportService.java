package com.example.matrimony.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBugReportService {

    private final AdminBugReportRepository repository;
    private final DevNotificationEmailService emailService;

    public AdminBugReportService(AdminBugReportRepository repository,
                                 DevNotificationEmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Transactional
    public AdminBugReport reportBug(AdminBugReportRequest request) {

        AdminBugReport bug = new AdminBugReport();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setSeverity(request.getSeverity());
        bug.setReportedBy(request.getReportedBy());

        AdminBugReport saved = repository.save(bug);

        // Notify dev team
        emailService.notifyDevTeam(saved);

        return saved;
    }
}