package com.example.matrimony.dto;

import java.time.LocalDateTime;
import com.example.matrimony.entity.ChatMessage;

public class ChatMessageDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime timestamp;
    private boolean seen;
    private LocalDateTime seenAt;

    public ChatMessageDto() {}

    public ChatMessageDto(Long id, Long senderId, Long receiverId,
                          String message, LocalDateTime timestamp,
                          boolean seen, LocalDateTime seenAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
        this.seen = seen;
        this.seenAt = seenAt;
    }

    public static ChatMessageDto fromEntity(ChatMessage cm) {
        return new ChatMessageDto(
                cm.getId(),
                cm.getSender().getId(),
                cm.getReceiver().getId(),
                cm.getMessage(),
                cm.getTimestamp(),
                cm.isSeen(),
                cm.getSeenAt()
        );
    }

	public synchronized Long getId() {
		return id;
	}

	public synchronized void setId(Long id) {
		this.id = id;
	}

	public synchronized Long getSenderId() {
		return senderId;
	}

	public synchronized void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public synchronized Long getReceiverId() {
		return receiverId;
	}

	public synchronized void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public synchronized String getMessage() {
		return message;
	}

	public synchronized void setMessage(String message) {
		this.message = message;
	}

	public synchronized LocalDateTime getTimestamp() {
		return timestamp;
	}

	public synchronized void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public synchronized boolean isSeen() {
		return seen;
	}

	public synchronized void setSeen(boolean seen) {
		this.seen = seen;
	}

	public synchronized LocalDateTime getSeenAt() {
		return seenAt;
	}

	public synchronized void setSeenAt(LocalDateTime seenAt) {
		this.seenAt = seenAt;
	}

}