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
       Used to decide who can UNBLOCK
       ===================================================== */
    boolean existsByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

    /* =====================================================
       IS THERE ANY BLOCK BETWEEN TWO USERS? (Bidirectional)
       Used for chat/message restriction
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
       UNBLOCK (ONLY removes one direction)
       Used ONLY after permission check in service
       ===================================================== */
    @Transactional
    @Modifying
    void deleteByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

    /* =====================================================
       OPTIONAL – FORCE DELETE BOTH DIRECTIONS (ADMIN / SAFETY)
       ===================================================== */
    @Transactional
    @Modifying
    @Query("""
        DELETE FROM UserBlock ub
        WHERE
          (ub.blocker.id = :userA AND ub.blocked.id = :userB)
          OR
          (ub.blocker.id = :userB AND ub.blocked.id = :userA)
    """)
    void deleteBlockBetween(
            @Param("userA") Long userA,
            @Param("userB") Long userB
    );
}
