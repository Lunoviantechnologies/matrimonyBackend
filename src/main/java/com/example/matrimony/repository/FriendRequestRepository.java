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
    	    WHERE fr.status = 'ACCEPTED'
    	    ORDER BY fr.sentAt DESC
    	""")
    	List<FriendRequest> findAllAcceptedWithSenderAndReceiver();

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
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage c WHERE c.sender.id = :id OR c.receiver.id = :id")
    void deleteBySenderOrReceiver(@Param("id") Long id);

	boolean existsBySender_IdAndReceiver_IdAndStatus(Long senderId, Long receiverId, Status pending);
	
	@Query("""
			SELECT 
			    CASE 
			        WHEN fr.sender.id = :userId THEN fr.receiver.id
			        ELSE fr.sender.id
			    END
			FROM FriendRequest fr
			WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId)
			AND fr.status IN ('PENDING','ACCEPTED','REJECTED')
			""")
			List<Long> findHiddenProfileIds(@Param("userId") Long userId);
	
	// received pending requests
	@Query("SELECT COUNT(fr) FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status = 'PENDING'")
	long countReceived(@Param("userId") Long userId);

	// accepted (both sent & received)
	@Query("""
	SELECT COUNT(fr) FROM FriendRequest fr 
	WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId)
	AND fr.status = 'ACCEPTED'
	""")
	long countAccepted(@Param("userId") Long userId);

	// rejected (both sent & received)
	@Query("""
	SELECT COUNT(fr) FROM FriendRequest fr 
	WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId)
	AND fr.status = 'REJECTED'
	""")
	long countRejected(@Param("userId") Long userId);

	// sent pending requests
	@Query("SELECT COUNT(fr) FROM FriendRequest fr WHERE fr.sender.id = :userId AND fr.status = 'PENDING'")
	long countSent(@Param("userId") Long userId);
	
	List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequest.Status status);

    List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequest.Status status);

    List<FriendRequest> findBySenderIdOrReceiverIdAndStatus(
            Long senderId, Long receiverId, FriendRequest.Status status);
    
    @Query("""
    		SELECT fr FROM FriendRequest fr
    		WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId)
    		AND fr.status = 'ACCEPTED'
    		""")
    		List<FriendRequest> findAcceptedBoth(@Param("userId") Long userId);


    		@Query("""
    		SELECT fr FROM FriendRequest fr
    		WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId)
    		AND fr.status = 'REJECTED'
    		""")
    		List<FriendRequest> findRejectedBoth(@Param("userId") Long userId);
    		// sent pending
    		List<FriendRequest> findBySender_IdAndStatus(Long senderId, FriendRequest.Status status);
    		
    		@Query("SELECT fr.receiver.id FROM FriendRequest fr WHERE fr.sender.id = :myId AND fr.status = 'ACCEPTED'")
    		List<Long> findAcceptedFriendIds(@Param("myId") Long myId);

    		@Query("SELECT fr.receiver.id FROM FriendRequest fr WHERE fr.sender.id = :myId AND fr.status = 'PENDING'")
    		List<Long> findSentRequestIds(@Param("myId") Long myId);

    		@Query("SELECT fr.sender.id FROM FriendRequest fr WHERE fr.receiver.id = :myId AND fr.status = 'PENDING'")
    		List<Long> findReceivedRequestIds(@Param("myId") Long myId);
    		
    		@Query("""
    			    SELECT 
    			        CASE 
    			            WHEN fr.sender.id = :myId THEN fr.receiver.id
    			            ELSE fr.sender.id
    			        END
    			    FROM FriendRequest fr
    			    WHERE (fr.sender.id = :myId OR fr.receiver.id = :myId)
    			      AND fr.status = 'REJECTED'
    			""")
    			List<Long> findRejectedIds(@Param("myId") Long myId);
    		@Query("""
    				SELECT fr FROM FriendRequest fr
    				JOIN FETCH fr.sender s
    				JOIN FETCH fr.receiver r
    				WHERE fr.status = 'ACCEPTED'
    				AND (s.id = :userId OR r.id = :userId)
    				""")
    				List<FriendRequest> findAllAccepted(@Param("userId") Long userId);
    		@Query("""
    				SELECT COUNT(m) FROM ChatMessage m
    				WHERE 
    				(m.sender.id = :user1 AND m.receiver.id = :user2)
    				OR
    				(m.sender.id = :user2 AND m.receiver.id = :user1)
    				""")
    				long countConversationMessages(
    				    @Param("user1") Long user1,
    				    @Param("user2") Long user2
    				);
}
