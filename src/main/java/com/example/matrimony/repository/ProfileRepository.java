package com.example.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.matrimony.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>,JpaSpecificationExecutor<Profile>{
	Optional<Profile> findByEmailId(String email);

    boolean existsByEmailId(String email);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmailIdAndIdNot(String emailId, Long id);

    boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);
//  3-day expiry reminder
    List<Profile> findByPremiumTrueAndPremiumEndAfterAndPremiumEndBefore(
            LocalDateTime start,
            LocalDateTime end
    );
    

    // fetch profiles with payments to map latestPayment safely
    @Query("select distinct p from Profile p left join fetch p.payments")
    List<Profile> findAllWithPayments();
    
    List<Profile> findByDeleteRequestedTrueAndDeleteRequestedAtBefore(
            LocalDateTime time
    );
    
    List<Profile> findByPremiumTrueAndPremiumEndBefore(LocalDateTime time);
    
    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM profile_friends
        WHERE profile_id = :profileId
           OR friend_id = :profileId
    """, nativeQuery = true)
    void deleteAllByProfileId(@Param("profileId") Long profileId);
    
    @Modifying
    @Transactional
    @Query("update Profile p set p.lastActive = :time where p.id = :id")
    void updateLastActive(@Param("id") Long id,
                          @Param("time") LocalDateTime time);

    // ===== Referral support (Refer & Earn) =====

    boolean existsByReferralCode(String referralCode);

    Optional<Profile> findByReferralCode(String referralCode);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM profile_friends WHERE profile_id = :id OR friend_id = :id", nativeQuery = true)
    void deleteFriendMappings(Long id);

   
     Page<Profile> findByAccountStatus(String accountStatus, Pageable pageable);

        // ===== Dashboard Stats =====

        long countByActiveTrue();
        long countByActiveFalse();
        long countByPremiumTrue();
        long countByPremiumFalse();
    
        long countByCreatedAtAfter(LocalDateTime time);
        Page<Profile> findAllByOrderByCreatedAtDesc(Pageable pageable);
        
        
        @Query("""
        		SELECT p FROM Profile p
        		WHERE lower(p.gender)=:gender
        		AND p.id<>:myId
        		AND p.approved=true
        		AND p.banned=false
        		AND (p.deleteRequested=false OR p.deleteRequested IS NULL)
        		AND p.id NOT IN :hiddenIds
        		AND p.createdAt>=:date
        		ORDER BY p.createdAt DESC
        		""")
        		List<Profile> findDashboardProfiles(
        		        String gender,
        		        Long myId,
        		        List<Long> hiddenIds,
        		        LocalDateTime date		        
        		);
        
        @Query("""
        		SELECT p FROM Profile p
        		WHERE lower(p.gender)=:gender
        		AND p.premiumEnd >= CURRENT_TIMESTAMP
        		AND p.id<>:myId
        		AND p.approved=true
        		AND p.banned=false
        		AND (p.deleteRequested=false OR p.deleteRequested IS NULL)
        		AND p.id NOT IN :hiddenIds
        		ORDER BY p.lastActive DESC
        		""")
        		List<Profile> findPremiumDashboardProfiles(
        		        @Param("gender") String gender,
        		        @Param("myId") Long myId,
        		        @Param("hiddenIds") List<Long> hiddenIds
        		);
        @Query("""
        		SELECT p FROM Profile p
        		WHERE lower(p.gender) = :gender
        		AND p.id <> :myId
        		AND p.approved = true
        		AND p.banned = false
        		AND (p.deleteRequested = false OR p.deleteRequested IS NULL)
        		AND p.id NOT IN :hiddenIds
        		ORDER BY p.premium DESC, p.lastActive DESC
        		""")
        		Page<Profile> findRecommendedProfiles(
        		        @Param("gender") String gender,
        		        @Param("myId") Long myId,
        		        @Param("hiddenIds") List<Long> hiddenIds,
        		        Pageable pageable
        		);
}
 