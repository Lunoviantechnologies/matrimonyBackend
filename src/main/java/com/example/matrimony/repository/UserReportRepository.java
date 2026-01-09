package com.example.matrimony.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.ReportStatus;
import com.example.matrimony.entity.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    List<UserReport> findByReportedUserId(Long userId);

    List<UserReport> findByStatus(ReportStatus status);
}