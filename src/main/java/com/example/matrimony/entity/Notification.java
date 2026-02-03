package com.example.matrimony.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(columnDefinition = "text")
    private String message;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String data; // JSON: Hibernate binds as JSON for PostgreSQL

    @Column(name = "is_read")
    private boolean isRead = false;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    // -------------------------------
    // Constructors
    // -------------------------------

    public Notification() {}

    public Notification(Long id, String type, String message, Long senderId, Long receiverId,
                        String data, boolean isRead, Instant createdAt) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Builder
    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public static class NotificationBuilder {
        private Long id;
        private String type;
        private String message;
        private Long senderId;
        private Long receiverId;
        private String data;
        private boolean isRead;
        private Instant createdAt = Instant.now();

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder type(String type) { this.type = type; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder senderId(Long senderId) { this.senderId = senderId; return this; }
        public NotificationBuilder receiverId(Long receiverId) { this.receiverId = receiverId; return this; }
        public NotificationBuilder data(String data) { this.data = data; return this; }
        public NotificationBuilder isRead(boolean isRead) { this.isRead = isRead; return this; }
        public NotificationBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public Notification build() {
            return new Notification(id, type, message, senderId, receiverId, data, isRead, createdAt);
        }
    }

    // -------------------------------
    // Getters and Setters
    // -------------------------------

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public Long getSenderId() { return senderId; }

    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }

    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public void setCreatedAt(LocalDateTime now) {
		// TODO Auto-generated method stub
		
	}
}
