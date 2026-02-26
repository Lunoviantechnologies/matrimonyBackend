package com.example.matrimony.dto;

import java.time.LocalDateTime;

public class ChatContactDto {

    private Long userId;
    private String name;
    private String photo;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private boolean online;
    private boolean blockedByMe;
    private boolean blockedByOther;

    public ChatContactDto(Long userId, String name, String photo,
                          String lastMessage, LocalDateTime lastMessageTime,
                          boolean online,
                          boolean blockedByMe,
                          boolean blockedByOther) {
        this.userId = userId;
        this.name = name;
        this.photo = photo;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.online = online;
        this.blockedByMe = blockedByMe;
        this.blockedByOther = blockedByOther;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public LocalDateTime getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(LocalDateTime lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isBlockedByMe() {
		return blockedByMe;
	}

	public void setBlockedByMe(boolean blockedByMe) {
		this.blockedByMe = blockedByMe;
	}

	public boolean isBlockedByOther() {
		return blockedByOther;
	}

	public void setBlockedByOther(boolean blockedByOther) {
		this.blockedByOther = blockedByOther;
	}

    
}
