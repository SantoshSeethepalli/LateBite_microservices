package com.backend.auth_services.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-exp-ms}")
    private Long exp;

    public String generateAccessToken(Long authUserId, Long refId, String role, String phone) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(String.valueOf(authUserId))
                .claim("refId", refId)
                .claim("role", role)
                .claim("phone", phone)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + exp))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validate(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
