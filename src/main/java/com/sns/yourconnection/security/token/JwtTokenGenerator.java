package com.sns.yourconnection.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    @Value("${jwt.token.key}")
    private String key;
    @Value("${jwt.access-expired}")
    private Long accessExpiredTimeMs;
    @Value("${jwt.refresh-expired}")
    private Long refreshExpiredTimeMs;


    public AccessToken generateAccessToken(String username) {
        return AccessToken.of(createJwtToken(username), BEARER_TYPE, accessExpiredTimeMs);
    }

    private String createJwtToken(String username) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiredTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return token;
    }

    public String createRefreshToken(String userName, Long expireTimeMs) {
        //TODO: refresh token 구현 중 수정 해야 함.
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiredTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getUsername(String token) {
        return extractClaim(token, key).get("username", String.class);
    }

    private Claims extractClaim(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public String extractSubject(String token) {
        return extractClaim(token, key).getSubject();
    }
}
