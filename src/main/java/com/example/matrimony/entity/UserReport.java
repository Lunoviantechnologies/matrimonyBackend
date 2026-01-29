package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "user_reports",
    indexes = {
        @Index(name = "idx_report_status", columnList = "status"),
        @Index(name = "idx_reported_user", columnList = "reported_user_id"),
        @Index(name = "idx_reporter", columnList = "reporter_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"reporter_id", "reported_user_id"})
    }
)
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Profile reporter;

    // Who is reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private Profile reportedUser;

    // Reason category
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ReportReason reason;
   

    // Optional description from reporter
    @Column(columnDefinition = "TEXT")
    private String description;

    // Moderation status
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    // When report created
    @Column(nullable = false, updatable = false)
    private LocalDateTime reportedAt;

    // When admin reviewed
    private LocalDateTime reviewedAt;

    // Admin comment / decision reason
    @Column(length = 500)
    private String adminComment;

    // ================= AUTO TIMESTAMP ================= //
    @PrePersist
    public void onCreate() {
        this.reportedAt = LocalDateTime.now();
    }

    // ================= GETTERS & SETTERS ================= //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    // Utility helper methods
    public Long getReporterId() {
        return reporter != null ? reporter.getId() : null;
    }

    public Long getReportedUserId() {
        return reportedUser != null ? reportedUser.getId() : null;
    }
}