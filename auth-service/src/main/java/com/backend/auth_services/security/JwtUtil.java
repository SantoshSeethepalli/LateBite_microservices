package com.backend.auth_services.security;

import io.jsonwebtoken.*;
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
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims validate(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
