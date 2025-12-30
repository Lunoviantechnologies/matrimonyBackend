package com.example.matrimony.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Base64;
//
//@Component
//public class JwtUtil {
//
//    @Value("${app.jwt.secret}")
//    private String secret;
//
//    @Value("${app.jwt.expiration-mins}")
//    private long expirationMins;
//
//    private SecretKey signingKey;
//
//    private SecretKey getSigningKey() {
//        if (signingKey != null) return signingKey;
//
//        byte[] keyBytes;
//        try {
//            keyBytes = Base64.getDecoder().decode(secret);
//        } catch (Exception e) {
//            keyBytes = secret.getBytes();
//        }
//
//        if (keyBytes.length < 32) {
//            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        } else {
//            signingKey = Keys.hmacShaKeyFor(keyBytes);
//        }
//        return signingKey;
//    }
//
//    // Generate JWT token with userId
//    public String generateToken(Long userId, String email, List<String> roles) {
//        long expiryMs = expirationMins * 60 * 1000;
//
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("userId", userId)   // ✅ add ID here
//                .claim("roles", roles)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiryMs))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Jws<Claims> validate(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token);
//    }
//
//    public String extractEmail(String token) {
//        return validate(token).getBody().getSubject();
//    }
//
//    public Long extractUserId(String token) {
//        return validate(token).getBody().get("userId", Long.class);
//    }
//
//    public List<String> extractRoles(String token) {
//        return validate(token).getBody().get("roles", List.class);
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        try {
//            String email = extractEmail(token);
//            Date expiration = validate(token).getBody().getExpiration();
//            return email.equals(userDetails.getUsername()) && expiration.after(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
