package com.example.matrimony.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity   // 🔴 MUST EXIST
@Table(name = "user_block")
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private Profile blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id")
    private Profile blocked;

    private LocalDateTime blockedAt = LocalDateTime.now();

	public synchronized Long getId() {
		return id;
	}

	public synchronized void setId(Long id) {
		this.id = id;
	}

	public synchronized Profile getBlocker() {
		return blocker;
	}

	public synchronized void setBlocker(Profile blocker) {
		this.blocker = blocker;
	}

	public synchronized Profile getBlocked() {
		return blocked;
	}

	public synchronized void setBlocked(Profile blocked) {
		this.blocked = blocked;
	}

	public synchronized LocalDateTime getBlockedAt() {
		return blockedAt;
	}

	public synchronized void setBlockedAt(LocalDateTime blockedAt) {
		this.blockedAt = blockedAt;
	}

	
}
