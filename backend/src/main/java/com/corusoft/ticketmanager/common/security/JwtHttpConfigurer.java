package com.corusoft.ticketmanager.common.security;

import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

// https://stackoverflow.com/a/71449312/11295728
@Component
@AllArgsConstructor
public class JwtHttpConfigurer extends AbstractHttpConfigurer<JwtHttpConfigurer, HttpSecurity> {
    private final JwtGenerator jwtGenerator;

    @Override
    public void configure(HttpSecurity http) {
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        JwtFilter jwtFilter = new JwtFilter(authenticationManager, jwtGenerator);
        http.addFilter(jwtFilter);
    }
}
