package com.example.matrimony.dto;

import com.example.matrimony.entity.ReportReason;

public class UserReportRequest {

    private Long reporterId;
    private ReportReason reason;
    private String description;

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
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
}
