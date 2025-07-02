package com.yhh.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static String secret;
    private static Long expiration;

    @Value("${jwt.secret}")
    public void setSecret(String secretKey) {
        secret = secretKey;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(Long expirationTime) {
        expiration = expirationTime;
    }

    private static Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, String username, boolean isAdmin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("isAdmin", isAdmin);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username", String.class);
    }

    public static boolean isAdmin(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("isAdmin", Boolean.class);
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        throw new RuntimeException("未登录或token已过期");
    }
} 