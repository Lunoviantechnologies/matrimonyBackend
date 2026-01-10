package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.FriendRequest;
import com.example.matrimony.entity.FriendRequest.Status;
import com.example.matrimony.entity.Profile;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Get a request between sender and receiver
    Optional<FriendRequest> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    // Check reverse direction
    Optional<FriendRequest> findByReceiver_IdAndSender_Id(Long receiverId, Long senderId);

    // All requests sent by a user
    List<FriendRequest> findBySender_Id(Long senderId);

    // All requests received by a user
    List<FriendRequest> findByReceiver_Id(Long receiverId);

    // Received requests with status
    List<FriendRequest> findByReceiver_IdAndStatus(Long receiverId, Status status);

    // Bidirectional (both ways) – for checking existing connection
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
           "(fr.sender = :profile1 AND fr.receiver = :profile2) OR " +
           "(fr.sender = :profile2 AND fr.receiver = :profile1)")
    List<FriendRequest> findBetweenProfiles(@Param("profile1") Profile profile1,
                                            @Param("profile2") Profile profile2);

    // Find all by status
    List<FriendRequest> findByStatus(Status status);

    // Delete friend requests involving a profile
    @Transactional
    void deleteBySenderIdOrReceiverId(Long senderId, Long receiverId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FriendRequest fr WHERE fr.sender.id = :profileId OR fr.receiver.id = :profileId")
    void deleteByProfileId(@Param("profileId") Long profileId);


    /* =====================================================
            ACCEPTED  (Received / Sent)
       ===================================================== */

    @Query("SELECT f FROM FriendRequest f WHERE f.receiver.id = :userId AND f.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedReceived(@Param("userId") Long userId);

    @Query("SELECT f FROM FriendRequest f WHERE f.sender.id = :userId AND f.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedSent(@Param("userId") Long userId);

    @Query("""
    	    SELECT fr
    	    FROM FriendRequest fr
    	    JOIN FETCH fr.sender
    	    JOIN FETCH fr.receiver
    	    ORDER BY fr.sentAt DESC
    	""")
    List<FriendRequest> findAllWithSenderAndReceiver();

    /* =====================================================
            REJECTED  (Received / Sent)
       ===================================================== */

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender.id = :userId AND fr.status = 'REJECTED'")
    List<FriendRequest> findRejectedSent(@Param("userId") Long userId);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status = 'REJECTED'")
    List<FriendRequest> findRejectedReceived(@Param("userId") Long userId);
    
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM FriendRequest fr
        WHERE fr.sender.id = :profileId
           OR fr.receiver.id = :profileId
    """)
    void deleteAllByProfileId(@Param("profileId") Long profileId);
}
