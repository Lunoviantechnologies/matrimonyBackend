package com.example.matrimony.admin;

import jakarta.validation.constraints.NotBlank;

public class AdminBugReportRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String severity; // LOW / MEDIUM / HIGH / CRITICAL

    @NotBlank
    private String reportedBy;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}

	@Override
	public String toString() {
		return "AdminBugReportRequest [title=" + title + ", description=" + description + ", severity=" + severity
				+ ", reportedBy=" + reportedBy + "]";
	}

   
}