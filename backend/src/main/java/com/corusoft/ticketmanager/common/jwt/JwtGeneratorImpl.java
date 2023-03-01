package com.corusoft.ticketmanager.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGeneratorImpl implements JwtGenerator {
    @Value("${project.jwt.signKey}")
    private String signKey;

    @Value("${project.jwt.expirationMinutes}")
    private long expirationMinutes;

    @Override
    public String generateJWT(JwtData data) {
        long expirationInMillis = expirationMinutes * 60 * 1000;
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + expirationInMillis);

        return Jwts.builder()
                .setExpiration(tokenExpirationDate)
                .signWith(Keys.hmacShaKeyFor(signKey.getBytes()), SignatureAlgorithm.HS256)
                //.signWith(SignatureAlgorithm.HS512, signKey.getBytes())
                // Información a almacenar en el JWT
                .claim("userID", data.getUserID())
                .claim("nickname", data.getNickname())
                .claim("role", data.getRole())
                .setIssuedAt(new Date())
                .compact();
    }

    @Override
    public JwtData extractInfoFromToken(String token) {
        SecretKey secret = Keys.hmacShaKeyFor(signKey.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Parsear datos obtenidos del JWT
        Long userID = Long.valueOf(claims.get("userID").toString());
        String userNickname = claims.get("nickName").toString();
        String userRole = claims.get("role").toString();


        return new JwtData(userID, userNickname, userRole);
    }

}
