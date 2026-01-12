package com.example.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ArchivedChatMessage;

public interface ArchivedChatRepository
extends JpaRepository<ArchivedChatMessage, Long> {
	   List<ArchivedChatMessage> 
       findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
           Long senderId1, Long receiverId1,
           Long senderId2, Long receiverId2
       );

   // ✅ Get archived chats of a deleted user
   List<ArchivedChatMessage> findByDeletedUserId(Long deletedUserId);
   
   @Modifying
   @Transactional
   @Query("""
       DELETE FROM ArchivedChatMessage ac
       WHERE ac.archivedAt < :expiryDate
   """)
   int deleteArchivedChatsOlderThan(LocalDateTime expiryDate);
}
