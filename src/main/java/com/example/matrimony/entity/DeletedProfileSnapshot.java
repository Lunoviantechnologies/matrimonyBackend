package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "deleted_profile_snapshot")
public class DeletedProfileSnapshot {

    @Id
    private Long userId;

    @Column(columnDefinition = "LONGTEXT")
    private String fullProfileJson;

    private LocalDateTime deletedAt;
    private String deletedByAdmin;
    private String deleteReason;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFullProfileJson() {
		return fullProfileJson;
	}
	public void setFullProfileJson(String fullProfileJson) {
		this.fullProfileJson = fullProfileJson;
	}
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
	public String getDeletedByAdmin() {
		return deletedByAdmin;
	}
	public void setDeletedByAdmin(String deletedByAdmin) {
		this.deletedByAdmin = deletedByAdmin;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	@Override
	public String toString() {
		return "DeletedProfileSnapshot [userId=" + userId + ", fullProfileJson=" + fullProfileJson + ", deletedAt="
				+ deletedAt + ", deletedByAdmin=" + deletedByAdmin + ", deleteReason=" + deleteReason + "]";
	}
    
}