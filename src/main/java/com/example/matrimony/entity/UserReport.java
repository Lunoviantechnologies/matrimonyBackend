package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "user_reports")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who reported
    @ManyToOne(optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Profile reporter;

    // Who is reported
    @ManyToOne(optional = false)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private Profile reportedUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    // ---------- getters & setters ----------

    public Long getId() {
        return id;
    }

    public Profile getReporter() {
        return reporter;
    }

    public void setReporter(Profile reporter) {
        this.reporter = reporter;
    }

    public Profile getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Profile reportedUser) {
        this.reportedUser = reportedUser;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }
}
