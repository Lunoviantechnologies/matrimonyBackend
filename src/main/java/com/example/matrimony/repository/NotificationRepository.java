package com.example.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.Notification;
import com.example.matrimony.service.Notificationadminservice;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);
    long countByReceiverIdAndIsReadFalse(Long receiverId);
    List<Notification> findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(Long receiverId);
   static void deleteByCreatedAtBefore(LocalDateTime dateTime) {
	// TODO Auto-generated method stub
	
}

void save(Notificationadminservice n);
}
