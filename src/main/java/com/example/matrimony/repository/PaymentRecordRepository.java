package com.example.matrimony.repository;

import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByRazorpayOrderId(String orderId);
    Optional<PaymentRecord> findByRazorpayPaymentId(String paymentId);
    
 // PaymentRecordRepository extends JpaRepository<PaymentRecord, Long>
    List<PaymentRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<PaymentRecord> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    List<PaymentRecord> findByStatusOrderByCreatedAtDesc(String status);

    Optional<PaymentRecord> findByIdAndStatus(Long id, String status);
    List<PaymentRecord> findByStatusInOrderByCreatedAtDesc(List<String> statuses);

}
