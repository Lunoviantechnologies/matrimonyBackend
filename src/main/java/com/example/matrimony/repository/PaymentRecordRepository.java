package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


}
