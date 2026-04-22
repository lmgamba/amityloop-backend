package com.lmgamba.amityloop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// Handles all JWT operations: generation and validation
@Service
public class JwtService {

    // Reads jwt.secret from application.properties (which reads from environment variable)
    @Value("${jwt.secret}")
    private String secret;

    // Reads jwt.expiration from application.properties (milliseconds)
    @Value("${jwt.expiration}")
    private long expiration;

    // Converts the secret string into a cryptographic key for signing
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generates a JWT token for a given email (used after successful login)
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Extracts the email from a valid token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Checks if the token is valid and not expired
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Parses the token and returns its claims (payload data)
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}