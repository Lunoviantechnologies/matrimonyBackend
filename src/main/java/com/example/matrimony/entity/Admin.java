package com.example.matrimony.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Email
    @Column(nullable = false, unique = true)
    private String emailId;  // corrected

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String createPassword; // corrected
    @Column(nullable = false)
    private String adminRole;
    

    public synchronized String getAdminRole() {
		return adminRole;
	}

	public synchronized void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}

	public Admin() {}

    public Admin(String username, String emailId, String createPassword) {
        this.username = username;
        this.emailId = emailId;
        this.createPassword = createPassword;
    }

//    public Long getId() {
//        return Id;
//    }
//
//    public void setId(Long id) {
//        this.Id = id;
//    }
    

    public String getEmailId() {
        return emailId;
    }

    public synchronized Long getAdminId() {
		return adminId;
	}

	public synchronized void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatePassword() {
        return createPassword;
    }

    public void setCreatePassword(String createPassword) {
        this.createPassword = createPassword;
    }
}
