package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_reports")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who reported
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Profile reporter;

    // Who is reported
    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    private Profile reportedUser;

    // Reason category
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    // Optional message
    @Column(columnDefinition = "TEXT")
    private String description;

    // Status for admin
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    private LocalDateTime reportedAt = LocalDateTime.now();

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

	public Long getReporterId() {
		// TODO Auto-generated method stub
		return null;
	}

	
    
    
}