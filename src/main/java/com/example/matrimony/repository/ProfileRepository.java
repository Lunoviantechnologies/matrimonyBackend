package com.example.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
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

     
}
 