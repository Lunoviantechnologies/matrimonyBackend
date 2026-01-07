package com.example.matrimony.admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBugReportRepository
        extends JpaRepository<AdminBugReport, Long> {
}