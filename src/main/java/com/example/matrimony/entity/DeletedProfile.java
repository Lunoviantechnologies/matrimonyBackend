package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "deleted_profiles")
public class DeletedProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // original profile id
    @Column(nullable = false)
    private Long originalProfileId;

    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String religion;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    // why deleted (optional)
    private String deleteReason;

    public DeletedProfile() {}

    // getters & setters
    // ------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOriginalProfileId() { return originalProfileId; }
    public void setOriginalProfileId(Long originalProfileId) {
        this.originalProfileId = originalProfileId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public String getDeleteReason() { return deleteReason; }
    public void setDeleteReason(String deleteReason) { this.deleteReason = deleteReason; }
}
