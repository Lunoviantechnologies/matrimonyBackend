package com.example.matrimony.hrms;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

//==
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil1 {
//
//    @Value("${app.jwt.secret}")
//    private String secret;
//
//    @Value("${app.jwt.expiration}") // in minutes
//    private long expirationTime;
//
//    @Value("${app.jwt.refreshExpiration}") // in minutes
//    private long refreshExpirationTime;
//
//    private SecretKey key;
//
//    @PostConstruct
//    public void init() {
//        // HS512 requires at least 64-byte key
//        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    // Generate JWT token
//    public String generateToken(Long id, String email, List<String> roles) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("id", id)
//                .claim("roles", roles)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 60 * 1000)) // minutes to ms
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    // Generate Refresh Token
//    public String generateRefreshToken(Long id, String email, String role) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("id", id)
//                .claim("role", role)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime * 60 * 1000))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }

    private final String secret = "mySuperSecretKeyThatIsAtLeast64CharactersLongForHS512Encryption!!!123456";
    private final long expirationTime = 60 * 60 * 1000; // 60 minutes

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate JWT token
    public String generateToken(Long id, String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Extract all claims
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract email (subject) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // Check expiration
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }


}
