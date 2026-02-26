package com.example.matrimony.dto;

import java.time.LocalDateTime;
import com.example.matrimony.entity.ReportReason;
import com.example.matrimony.entity.ReportStatus;
import java.util.List;
import com.example.matrimony.dto.ChatMessageDto;

public class UserReportResponse {

    private Long id;

    private Long reporterId;
    private String reporterName;

    private Long reportedUserId;
    private String reportedUserName;

    private ReportReason reason;
    private String description;
    private ReportStatus status;

    private LocalDateTime reportedAt;
    private LocalDateTime reviewedAt;

    private String adminComment;

    // ===== getters setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public Long getReportedUserId() { return reportedUserId; }
    public void setReportedUserId(Long reportedUserId) { this.reportedUserId = reportedUserId; }

    public String getReportedUserName() { return reportedUserName; }
    public void setReportedUserName(String reportedUserName) { this.reportedUserName = reportedUserName; }

    public ReportReason getReason() { return reason; }
    public void setReason(ReportReason reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getAdminComment() { return adminComment; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }
}