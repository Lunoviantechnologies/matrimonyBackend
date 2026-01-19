package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ReportStatus;
import com.example.matrimony.entity.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    List<UserReport> findByReportedUserId(Long userId);
    
    Optional<UserReport> findById(Long id);

    List<UserReport> findByStatus(ReportStatus status);
    @Modifying
    @Transactional
    void deleteAllByReportedUserId(Long userId);

	
}