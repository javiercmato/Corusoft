package com.corusoft.ticketmanager.common.security;

import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JwtFilter extends BasicAuthenticationFilter {
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";
    private static final String ROLE_PREFIX = "ROLE_";
    public static final String SERVICE_TOKEN_ATTRIBUTE_NAME = "serviceToken";
    public static final String USER_ID_ATTRIBUTE_NAME = "userID";

    private final JwtGenerator jwtGenerator;


    public JwtFilter(AuthenticationManager authManager, JwtGenerator jwtGenerator) {
        super(authManager);
        this.jwtGenerator = jwtGenerator;
    }


    /**
     * Obtiene el JWT de la cabecera HTTP AUTHORIZATION y se lo comunica a Spring para que configure el contexto del usuario
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Comprueba si se ha recibido el JWT en la cabecera de la petición
        String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaderValue == null || !authHeaderValue.startsWith(AUTH_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // Al obtener el JWT, extrae sus atributos y se los comunica a Spring
        UsernamePasswordAuthenticationToken authToken = getAuthentication(request);       // Obtiene los datos de acceso
        SecurityContextHolder.getContext()
                .setAuthentication(authToken);                               // Configura Spring con los datos recibidos

        // Filtra la petición según la configuración recién establecida
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // Elimina el "Bearer " de la cabecera
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.replace(AUTH_TOKEN_PREFIX, "");

        // Extraer los datos del token
        JwtData data = jwtGenerator.extractInfoFromToken(token);

        // Si se obtienen los datos, se registran
        if (data != null) {
            request.setAttribute(SERVICE_TOKEN_ATTRIBUTE_NAME, token);
            request.setAttribute(USER_ID_ATTRIBUTE_NAME, data.getUserID());
            // Asigna rol al usuario
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + data.getRole()));

            return new UsernamePasswordAuthenticationToken(data, null, authorities);
        } else
            return null;
    }
}
