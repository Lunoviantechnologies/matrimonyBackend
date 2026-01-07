package com.example.matrimony.dto;

public class BlockUserResponse {

    private boolean blocked;
    private Long blockerId;
    private Long blockedId;

    public BlockUserResponse(boolean blocked, Long blockerId, Long blockedId) {
        this.blocked = blocked;
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public Long getBlockerId() {
        return blockerId;
    }

    public Long getBlockedId() {
        return blockedId;
    }
}
