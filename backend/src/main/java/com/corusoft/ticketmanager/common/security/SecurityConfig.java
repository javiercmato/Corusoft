package com.corusoft.ticketmanager.common.security;

import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /* Cómo obtener el AuthenticationManager: https://stackoverflow.com/a/71449312/11295728 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtGenerator jwtGenerator) throws Exception {
        // Crear un filtro que procese el JWT que hemos creado
        JwtHttpConfigurer jwtConfigurer = new JwtHttpConfigurer(jwtGenerator);

        http
                // Desactivar CSRF porque no usamos
                .cors().and().csrf().disable()
                // No guardar datos de la sesión del usuario
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Aplicar filtro creado para poder usar JWT
                .and().apply(jwtConfigurer)
                // Permitir las peticiones que indiquemos
                .and().authorizeHttpRequests()
                // USER ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()                        // register
                .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()                           // login
                .requestMatchers(HttpMethod.POST, "/api/users/login/token").permitAll()                     // loginFromToken
                .requestMatchers(HttpMethod.POST, "/api/users/subscribe/*").permitAll()                     // subscribeToPremium
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()                                  // findUserByNameOrNickname

                // TICKET ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/tickets/categories").permitAll()                     // getAllCategories
                .requestMatchers(HttpMethod.POST, "/api/tickets/categories").permitAll()                    // createCustomizedCategory
                .requestMatchers(HttpMethod.PUT, "/api/tickets/categories/*").permitAll()                   // updateCustomizedCategory
                .requestMatchers(HttpMethod.GET, "/api/tickets/categories/*").permitAll()                   // getCustomizedCategoriesByUser
                .requestMatchers(HttpMethod.POST, "/api/tickets/parse").permitAll()                         // parseTicket
                .requestMatchers(HttpMethod.POST, "/api/tickets/").permitAll()                              // createTicket
                .requestMatchers(HttpMethod.DELETE, "/api/tickets/*").permitAll()                           // deleteTicket
                .requestMatchers(HttpMethod.POST, "/api/tickets/share/*").permitAll()                       // shareTicket
                .requestMatchers(HttpMethod.GET, "/api/tickets/*").permitAll()                              // getTicketDetails
                .requestMatchers(HttpMethod.PUT, "/api/tickets/*").permitAll()                              // filterUserTicketsByCriteria
                .requestMatchers(HttpMethod.GET, "/api/tickets/sharedTickets").permitAll()

                // STATISTICS ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/stats/spendingsPerMonth").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/stats/wastesCategory").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stats/spendingsThisMonth").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stats/percentagePerCategory").permitAll()

                // DENEGAR EL RESTO DE PETICIONES
                .anyRequest().denyAll();

        return http.build();
    }


    /**
     * Configuración de seguridad para permitir peticiones CORS.
     * También controla qué tipo de contenido aceptar en las peticiones (origen, cabeceras, método HTTP, etc)
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");            // Permite peticiones de cualquier origen
        config.addAllowedMethod("*");            // Permite peticiones con cualquier verbo HTTP
        config.addAllowedHeader("*");            // Permite peticiones con cualquier cabecera

        // Aplica la configuración a todas las URL expuestas por el servicio
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}

