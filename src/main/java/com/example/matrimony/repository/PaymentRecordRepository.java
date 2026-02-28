package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.matrimony.dto.PlanYearlyStats;
import com.example.matrimony.entity.PaymentRecord;

import jakarta.transaction.Transactional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByRazorpayOrderId(String orderId);
    Optional<PaymentRecord> findByRazorpayPaymentId(String paymentId);
    
 // PaymentRecordRepository extends JpaRepository<PaymentRecord, Long>
    List<PaymentRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<PaymentRecord> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    List<PaymentRecord> findByStatusOrderByCreatedAtDesc(String status);

    Optional<PaymentRecord> findByIdAndStatus(Long id, String status);
    List<PaymentRecord> findByStatusInOrderByCreatedAtDesc(List<String> statuses);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM PaymentRecord p WHERE p.profile.id = :id")
    void deleteByProfileId(@Param("id") Long id);
    
 // ===== Dashboard Revenue =====

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentRecord p WHERE p.status = 'PAID'")
    Double getTotalRevenue();

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM PaymentRecord p
        WHERE p.status = 'PAID'
        AND FUNCTION('DATE', p.createdAt) = CURRENT_DATE
    """)
    Double getTodayRevenue();
    
    @Query("""
    	    SELECT 
    	        p.planName AS planType,
    	        COUNT(p.id) AS totalMembers,
    	        COALESCE(SUM(p.amount), 0) AS totalRevenue
    	    FROM PaymentRecord p
    	    WHERE p.status = 'PAID'
    	      AND FUNCTION('YEAR', p.createdAt) = :year
    	    GROUP BY p.planName
    	""")
    	List<PlanYearlyStats> getYearlyPlanStats(@Param("year") int year);
    @Query("""
    	    SELECT p FROM PaymentRecord p
    	    WHERE p.userId = :userId
    	    AND p.status IN ('PAID','SUCCESS')
    	    AND p.premiumEnd >= CURRENT_TIMESTAMP
    	    ORDER BY p.premiumEnd DESC
    	""")
    	Optional<PaymentRecord> findActiveSubscription(@Param("userId") Long userId);
    List<PaymentRecord> findByProfileId(Long profileId);
    List<PaymentRecord> findByUserIdAndStatus(Long userId, String status);


}
