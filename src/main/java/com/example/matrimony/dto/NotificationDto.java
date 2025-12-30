package com.example.matrimony.dto;

import java.time.Instant;

public class NotificationDto {

    private Long id;
    private String type;
    private String message;
    private Long senderId;
    private Long receiverId;
    private Object data;
    private boolean isRead;
    private Instant createdAt;
    private Long adminId;

    // -------------------------------
    // Constructors
    // -------------------------------

    public NotificationDto() {}

    public NotificationDto(Long id, String type, String message, Long senderId,
                           Long receiverId, Object data, boolean isRead, Instant createdAt) {
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
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String type;
        private String message;
        private Long senderId;
        private Long receiverId;
        private Object data;
        private boolean isRead;
        private Instant createdAt = Instant.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder senderId(Long senderId) { this.senderId = senderId; return this; }
        public Builder receiverId(Long receiverId) { this.receiverId = receiverId; return this; }
        public Builder data(Object data) { this.data = data; return this; }
        public Builder isRead(boolean isRead) { this.isRead = isRead; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public NotificationDto build() {
            return new NotificationDto(id, type, message, senderId, receiverId, data, isRead, createdAt);
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

    public Object getData() { return data; }

    public void setData(Object data) { this.data = data; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public synchronized Long getAdminId() {
		return adminId;
	}

	public synchronized void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	
}
