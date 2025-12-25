package com.neo.bank.service;

import com.neo.bank.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${spring.application.key}")
    private String key;

    @Value("${spring.application.token.expiry}")
    private int tokenExpiry;

    public String generateToken(String username, Role role){
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", username);
        claims.put("role", role.name());
        return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis()+tokenExpiry)).and().signWith(getKey()).compact();
    }
    public String extractUsername(String token){return extractClaim(token,Claims::getSubject);}

    public Role extractRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return Role.valueOf(role);
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Key getKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
