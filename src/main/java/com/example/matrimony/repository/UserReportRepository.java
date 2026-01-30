package com.example.matrimony.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ReportStatus;
import com.example.matrimony.entity.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    // ================= BASIC QUERIES =================
    List<UserReport> findByReportedUser_Id(Long userId);

    List<UserReport> findByStatus(ReportStatus status);

    Page<UserReport> findByStatus(ReportStatus status, Pageable pageable);

    List<UserReport> findByReportedUser_IdAndStatus(Long userId, ReportStatus status);

    boolean existsByReporter_IdAndReportedUser_Id(Long reporterId, Long reportedUserId);

    // ================= UPDATE STATUS ONLY =================
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
        UPDATE UserReport r 
        SET r.status = :status,
            r.reviewedAt = CURRENT_TIMESTAMP
        WHERE r.reportedUser.id = :userId
    """)
    int updateStatusByReportedUser(@Param("userId") Long userId,
                                   @Param("status") ReportStatus status);

    // ================= UPDATE STATUS + ADMIN COMMENT =================
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
        UPDATE UserReport r 
        SET r.status = :status,
            r.adminComment = :comment,
            r.reviewedAt = CURRENT_TIMESTAMP
        WHERE r.reportedUser.id = :userId
    """)
    int updateStatusAndCommentByReportedUser(@Param("userId") Long userId,
                                             @Param("status") ReportStatus status,
                                             @Param("comment") String comment);

    // ================= HARD DELETE =================
    @Modifying
    @Transactional
    void deleteAllByReportedUser_Id(Long userId);
}
