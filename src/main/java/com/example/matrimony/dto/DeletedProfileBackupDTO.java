package com.example.matrimony.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DeletedProfileBackupDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String gender;
    private LocalDateTime createdAt;

    private List<ChatMessageDto> chats;

    private LocalDateTime deletedAt;
    private String deletedByAdmin;
    private String deleteReason;

    // getters & setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ChatMessageDto> getChats() { return chats; }
    public void setChats(List<ChatMessageDto> chats) { this.chats = chats; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public String getDeletedByAdmin() { return deletedByAdmin; }
    public void setDeletedByAdmin(String deletedByAdmin) { this.deletedByAdmin = deletedByAdmin; }

    public String getDeleteReason() { return deleteReason; }
    public void setDeleteReason(String deleteReason) { this.deleteReason = deleteReason; }
}
