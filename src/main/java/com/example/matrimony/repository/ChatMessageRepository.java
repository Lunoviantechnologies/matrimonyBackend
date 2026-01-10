package com.example.matrimony.repository;

import com.example.matrimony.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // ===============================
    // CHAT PAGINATION (MAIN METHOD)
    // ===============================
    @Query("""
        SELECT m FROM ChatMessage m
        WHERE
          (m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.clearedBySender = false)
          OR
          (m.sender.id = :receiverId AND m.receiver.id = :senderId AND m.clearedByReceiver = false)
        ORDER BY m.timestamp DESC
    """)
    Page<ChatMessage> findConversationForUser(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId,
            Pageable pageable
    );

    // ===============================
    // RECENT MESSAGES (OPTIONAL)
    // ===============================
    List<ChatMessage> findTop20ByOrderByTimestampDesc();

    // ===============================
    // CLEAR CHAT (FOR ONE USER)
    // ===============================
    @Modifying
    @Transactional
    @Query("""
        UPDATE ChatMessage m
        SET m.clearedBySender = true
        WHERE m.sender.id = :senderId
          AND m.receiver.id = :receiverId
    """)
    void clearChatAsSender(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE ChatMessage m
        SET m.clearedByReceiver = true
        WHERE m.sender.id = :receiverId
          AND m.receiver.id = :senderId
    """)
    void clearChatAsReceiver(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );

    // ===============================
    // DELETE (OPTIONAL / ADMIN ONLY)
    // ===============================
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM ChatMessage m
        WHERE m.sender.id = :profileId
           OR m.receiver.id = :profileId
    """)
    void deleteByProfileId(@Param("profileId") Long profileId);
    
    
    @Modifying
    @Transactional
    @Query("""
    UPDATE ChatMessage cm
    SET cm.seen = true, cm.seenAt = CURRENT_TIMESTAMP
    WHERE cm.sender.id = :senderId
      AND cm.receiver.id = :receiverId
      AND cm.seen = false
    """)
    int markMessagesAsSeen(Long senderId, Long receiverId);
    
    @Query("""
    		SELECT cm FROM ChatMessage cm
    		WHERE
    		(
    		 (cm.sender.id = :senderId AND cm.receiver.id = :receiverId)
    		 OR
    		 (cm.sender.id = :receiverId AND cm.receiver.id = :senderId)
    		)
    		ORDER BY cm.timestamp ASC
    		""")
    		List<ChatMessage> getConversationWithSeenStatus(
    		        Long senderId,
    		        Long receiverId
    		);
    @Query("""
            SELECT m FROM ChatMessage m
            WHERE
              (m.sender.id = :userA AND m.receiver.id = :userB)
              OR
              (m.sender.id = :userB AND m.receiver.id = :userA)
        """)
        List<ChatMessage> findChatBetweenUsers(Long userA, Long userB);

        void deleteBySenderIdOrReceiverId(Long senderId, Long receiverId);
    
}
