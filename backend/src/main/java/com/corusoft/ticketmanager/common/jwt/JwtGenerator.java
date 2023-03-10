package com.corusoft.ticketmanager.common.jwt;

public interface JwtGenerator {

    String generateJWT(JwtData data);

    JwtData extractInfoFromToken(String token);
}
