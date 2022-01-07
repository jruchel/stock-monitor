package com.jruchel.stockmonitor.security.jwt;

import com.jruchel.stockmonitor.config.security.JWTConfig;
import com.jruchel.stockmonitor.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JWTUtils {

    private final JWTConfig config;

    public String extractUsername(String token) throws SignatureException {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws SignatureException {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssuedAt(String token) throws SignatureException {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public boolean isExpired(String token) throws SignatureException {
        return extractExpiration(token).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws SignatureException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(token).getBody();
    }

    public String generateToken(User user) {
        return createToken(new HashMap<>(), user.getUsername());
    }

    public boolean validateToken(String token, UserDetails user) throws SignatureException {
        return extractUsername(token).equals(user.getUsername()) && !isExpired(token);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, config.getSecret())
                .compact();
    }
}