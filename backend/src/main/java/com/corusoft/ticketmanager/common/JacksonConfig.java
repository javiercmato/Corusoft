package com.corusoft.ticketmanager.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    public ObjectMapper configureObjectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new JacksonLocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new JacksonLocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new JacksonLocalDateDeserializer());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}


