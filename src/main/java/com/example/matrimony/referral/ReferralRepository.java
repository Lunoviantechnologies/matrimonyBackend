package com.example.matrimony.referral;

import com.example.matrimony.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository extends JpaRepository<Referral, Long> {

    List<Referral> findByReferrer(Profile referrer);

    Optional<Referral> findByReferred(Profile referred);

    boolean existsByReferrerAndReferredEmail(Profile referrer, String referredEmail);

    long countByReferrerAndStatus(Profile referrer, ReferralStatus status);
    Optional<Referral> findTopByReferrer_IdOrderByCreatedAtDesc(Long referrerId);
    long countByReferrer_Id(Long referrerId);

    long countByReferrer_IdAndStatus(Long referrerId, ReferralStatus status);
}

