package com.example.matrimony.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DevNotificationEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.dev.team.email}")
    private String devTeamEmail;

    public DevNotificationEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void notifyDevTeam(AdminBugReport bug) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(devTeamEmail);
        msg.setSubject("🐞 BUG REPORTED: " + bug.getTitle());

        msg.setText(
                "Severity: " + bug.getSeverity() + "\n" +
                "Reported By: " + bug.getReportedBy() + "\n\n" +
                "Description:\n" + bug.getDescription()
        );

        mailSender.send(msg);
    }
}