package com.example.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.UserBlock;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

    /* =====================================================
       DID A BLOCK B? (Directional)
       ===================================================== */
    boolean existsByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

    /* =====================================================
       IS THERE ANY BLOCK BETWEEN TWO USERS? (Bidirectional)
       ===================================================== */
    @Query("""
        SELECT COUNT(ub) > 0
        FROM UserBlock ub
        WHERE
          (ub.blocker.id = :blockerId AND ub.blocked.id = :blockedId)
          OR
          (ub.blocker.id = :blockedId AND ub.blocked.id = :blockerId)
    """)
    boolean isBlockedBetween(
            @Param("blockerId") Long blockerId,
            @Param("blockedId") Long blockedId
    );

    /* =====================================================
       UNBLOCK (single direction)
       ===================================================== */
//    @Transactional
//    @Modifying
//    @Query("""
//        DELETE FROM UserBlock ub
//        WHERE ub.blocker.id = :blockerId
//          AND ub.blocked.id = :blockedId
//    """)
//    void deleteByBlockerAndBlocked(
//            @Param("blockerId") Long blockerId,
//            @Param("blockedId") Long blockedId
//    );

    /* =====================================================
       ADMIN DELETE – ALL BLOCK REFERENCES FOR USER
       ===================================================== */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UserBlock b WHERE b.blocked.id = :userId")
    void deleteAllByBlockedId(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UserBlock b WHERE b.blocker.id = :userId")
    void deleteAllByBlockerId(@Param("userId") Long userId);
    
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM UserBlock ub
        WHERE ub.blocker.id = :blockerId
          AND ub.blocked.id = :blockedId
    """)
    void deleteByBlockerAndBlocked(
            @Param("blockerId") Long blockerId,
            @Param("blockedId") Long blockedId
    );

}
