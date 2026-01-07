package com.example.matrimony.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.UserBlock;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.repository.UserBlockRepository;

@Service
public class BlockService {

    private final UserBlockRepository blockRepository;
    private final ProfileRepository profileRepository;

    public BlockService(UserBlockRepository blockRepository,
                        ProfileRepository profileRepository) {
        this.blockRepository = blockRepository;
        this.profileRepository = profileRepository;
    }

    /* =====================================================
       BLOCK USER (Only one directional record is stored)
       ===================================================== */
    @Transactional
    public boolean blockUser(Long blockerId, Long blockedId) {

        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("You cannot block yourself");
        }

        // Already blocked (same direction)
        if (blockRepository.existsByBlocker_IdAndBlocked_Id(blockerId, blockedId)) {
            return true;
        }

        Profile blocker = profileRepository.findById(blockerId)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));

        Profile blocked = profileRepository.findById(blockedId)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        UserBlock block = new UserBlock();
        block.setBlocker(blocker);
        block.setBlocked(blocked);

        blockRepository.save(block);
        return true;
    }

    /* =====================================================
       UNBLOCK USER (ONLY blocker can unblock)
       ===================================================== */
    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {

        boolean iAmBlocker =
                blockRepository.existsByBlocker_IdAndBlocked_Id(blockerId, blockedId);

        if (!iAmBlocker) {
            throw new RuntimeException("You are not allowed to unblock this user");
        }

        blockRepository.deleteByBlocker_IdAndBlocked_Id(blockerId, blockedId);
    }

    /* =====================================================
       IS THERE ANY BLOCK BETWEEN TWO USERS (Bidirectional)
       Used for chat/message restrictions
       ===================================================== */
    public boolean isBlocked(Long userA, Long userB) {
        return blockRepository.isBlockedBetween(userA, userB);
    }

    /* =====================================================
       DID *I* BLOCK THE OTHER USER (Directional)
       Used for UI decisions (show unblock or not)
       ===================================================== */
    public boolean iBlocked(Long me, Long otherUser) {
        return blockRepository.existsByBlocker_IdAndBlocked_Id(me, otherUser);
    }
}
