package com.example.matrimony.hrms;

public class JwtResponse {
    private String token;
    private String role;
    private String email;
    private Long id;

    public JwtResponse(String token, String role, String email, Long id) {
        this.token = token;
        this.role = role;
        this.email = email;
        this.id = id;
    }

    // Getters
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public Long getId() { return id; }
}
