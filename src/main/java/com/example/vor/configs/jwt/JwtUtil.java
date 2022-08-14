package com.example.vor.configs.jwt;

import com.example.vor.domains.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    public String extractUsername(String authToken) {
        return getClaims(authToken).getSubject();
    }

    public Claims getClaims(String authToken) {
        byte[] key = Base64.getEncoder().encode(secret.getBytes());
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    public boolean validateToken(String authToken) throws RuntimeException {
        return getClaims(authToken).getExpiration().before(new Date());
    }

    public String generateToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", List.of(user.getRole()));

        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes())))
                .compact();
    }
}
