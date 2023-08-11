package com.example.demo.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {
    public static String generateToken(String email, String secretKey, Long expiredTimeMs){
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String secretKey){
        byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }
    public static boolean isExpired(String token, String secretKey){
        Date extractDate = extractClaims(token, secretKey).getExpiration();
        return extractDate.before(new Date());
    }
    public static String getEmailFromToken(String token, String secretKey){
       Claims claims = extractClaims(token, secretKey);
       return claims.get("email", String.class);
    }
    private static Claims extractClaims(String token, String secretKey){
        return Jwts.parserBuilder().setSigningKey(getKey(secretKey)).build()
                .parseClaimsJws(token)
                .getBody();
    }
}
