package com.example.matrimony.dto;

public class JwtResponse {

    private String token;
    private String role;
    private String email;
    private Long id; // <-- ADD THIS

    public JwtResponse(String token, String role, String email, Long id) {
        this.token = token;
        this.role = role;
        this.email = email;
        this.id = id;
    }

    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
