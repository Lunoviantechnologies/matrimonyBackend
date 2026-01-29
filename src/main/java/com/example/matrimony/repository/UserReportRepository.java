package com.example.matrimony.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ReportStatus;
import com.example.matrimony.entity.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    // ==============================
    // BASIC QUERIES
    // ==============================

    List<UserReport> findByReportedUser_Id(Long userId);

    List<UserReport> findByStatus(ReportStatus status);

    // ==============================
    // ADMIN DASHBOARD (PAGINATION)
    // ==============================

    Page<UserReport> findByStatus(ReportStatus status, Pageable pageable);

    // ==============================
    // FILTER BY USER + STATUS
    // ==============================

    List<UserReport> findByReportedUser_IdAndStatus(Long userId, ReportStatus status);

    // ==============================
    // DUPLICATE REPORT CHECK
    // ==============================

    boolean existsByReporter_IdAndReportedUser_Id(Long reporterId, Long reportedUserId);

    // ==============================
    // SOFT DELETE REPORTS (NOT HARD DELETE)
    // ==============================

    @Modifying
    @Transactional
    @Query("update UserReport r set r.status = 'ACTION_TAKEN' where r.reportedUser.id = :userId")
    void markReportsActionTakenByReportedUser(Long userId);

    // ==============================
    // HARD DELETE (SUPER ADMIN ONLY)
    // ==============================

    @Modifying
    @Transactional
    void deleteAllByReportedUser_Id(Long userId);
}