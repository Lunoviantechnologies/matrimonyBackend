package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


	@Entity
	@Table(name = "archived_chat_messages")
	public class ArchivedChatMessage {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Long senderId;
	    private Long receiverId;

	    @Column(length = 2000)
	    private String message;

	    private LocalDateTime sentAt;

	    private Long deletedUserId; // 🔥 important

	    private LocalDateTime archivedAt = LocalDateTime.now();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getSenderId() {
			return senderId;
		}

		public void setSenderId(Long senderId) {
			this.senderId = senderId;
		}

		public Long getReceiverId() {
			return receiverId;
		}

		public void setReceiverId(Long receiverId) {
			this.receiverId = receiverId;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public LocalDateTime getSentAt() {
			return sentAt;
		}

		public void setSentAt(LocalDateTime sentAt) {
			this.sentAt = sentAt;
		}

		public Long getDeletedUserId() {
			return deletedUserId;
		}

		public void setDeletedUserId(Long deletedUserId) {
			this.deletedUserId = deletedUserId;
		}

		public LocalDateTime getArchivedAt() {
			return archivedAt;
		}

		public void setArchivedAt(LocalDateTime archivedAt) {
			this.archivedAt = archivedAt;
		}
	

	}
