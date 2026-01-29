package com.example.matrimony.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_block") // ✅ MUST MATCH DB TABLE
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blocker_id", nullable = false)
    private Profile blocker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blocked_id", nullable = false)
    private Profile blocked;

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getBlocker() {
		return blocker;
	}

	public void setBlocker(Profile blocker) {
		this.blocker = blocker;
	}

	public Profile getBlocked() {
		return blocked;
	}

	public void setBlocked(Profile blocked) {
		this.blocked = blocked;
	}

	public LocalDateTime getBlockedAt() {
		return blockedAt;
	}

	public void setBlockedAt(LocalDateTime blockedAt) {
		this.blockedAt = blockedAt;
	}

	@Override
	public String toString() {
		return "UserBlock [id=" + id + ", blocker=" + blocker + ", blocked=" + blocked + ", blockedAt=" + blockedAt
				+ "]";
	}

   
}
