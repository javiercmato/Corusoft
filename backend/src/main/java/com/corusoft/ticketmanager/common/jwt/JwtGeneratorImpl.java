package com.corusoft.ticketmanager.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
                .signWith(SignatureAlgorithm.HS512, signKey.getBytes())
                // Información a almacenar en el JWT
                .claim("userID", data.getUserID())
                .claim("nickName", data.getNickname())
                .claim("role", data.getRole())
                .compact();
    }

    @Override
    public JwtData extractInfoFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        // Parsear datos obtenidos del JWT
        Long userID = Long.valueOf(claims.get("userID").toString());
        String userNickname = claims.get("nickName").toString();
        String userRole = claims.get("role").toString();


        return new JwtData(userID, userNickname, userRole);
    }

}
