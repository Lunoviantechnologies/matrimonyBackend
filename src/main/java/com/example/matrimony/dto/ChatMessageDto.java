package com.example.matrimony.dto;

import java.time.LocalDateTime;

import com.example.matrimony.entity.ChatMessage;

public class ChatMessageDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime timestamp;

    // ✅ Default constructor (REQUIRED for WebSocket / Jackson)
    public ChatMessageDto() {
    }

    // ✅ All-args constructor
    public ChatMessageDto(
            Long id,
            Long senderId,
            Long receiverId,
            String message,
            LocalDateTime timestamp
    ) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // ===== GETTERS & SETTERS (NO synchronized) =====

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // ===== ENTITY → DTO MAPPER (🔥 THIS FIXES YOUR LIVE CHAT 🔥) =====
    public static ChatMessageDto fromEntity(ChatMessage entity) {

        if (entity == null) return null;

        return new ChatMessageDto(
                entity.getId(),
                entity.getSender() != null ? entity.getSender().getId() : null,
                entity.getReceiver() != null ? entity.getReceiver().getId() : null,
                entity.getMessage(),
                entity.getTimestamp()
        );
    }

}