package com.example.matrimony.dto;

import java.time.LocalDateTime;
import com.example.matrimony.entity.ReportReason;
import com.example.matrimony.entity.ReportStatus;

public class UserReportResponse {

    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private ReportReason reason;
    private String description;
    private ReportStatus status;
    private LocalDateTime reportedAt;
    private String adminComment;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReporterId() {
		return reporterId;
	}
	public void setReporterId(Long reporterId) {
		this.reporterId = reporterId;
	}
	public Long getReportedUserId() {
		return reportedUserId;
	}
	public void setReportedUserId(Long reportedUserId) {
		this.reportedUserId = reportedUserId;
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
	public String getAdminComment() {
		return adminComment;
	}
	public void setAdminComment(String adminComment) {
		this.adminComment = adminComment;
	}

    
}