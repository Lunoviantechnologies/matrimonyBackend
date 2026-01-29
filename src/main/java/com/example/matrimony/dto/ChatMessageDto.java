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

    // ✅ MUST MATCH ChatMessage ENTITY
    public static ChatMessageDto fromEntity(ChatMessage cm) {
        return new ChatMessageDto(
                cm.getId(),
                cm.getSender().getId(),
                cm.getReceiver().getId(),
                cm.getMessage(),
                cm.getCreatedAt(),   // ✅ FIXED
                cm.isSeen(),
                cm.getSeenAt()
        );
    }

    // getters & setters (no synchronized needed)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }

    public LocalDateTime getSeenAt() { return seenAt; }
    public void setSeenAt(LocalDateTime seenAt) { this.seenAt = seenAt; }
}
