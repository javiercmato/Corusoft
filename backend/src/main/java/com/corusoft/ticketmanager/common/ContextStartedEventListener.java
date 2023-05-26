package com.corusoft.ticketmanager.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

import static com.corusoft.ticketmanager.TicketManagerApplication.TEMP_PATH;

/**
 * Componente encargado de gestionar directorios temporales en el ciclo de vida de Spring.
 * INFO: https://stackoverflow.com/a/59498133
 * INFO: https://www.baeldung.com/spring-events
 */
@Component
public class ContextStartedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        try {
            if (!Files.exists(TEMP_PATH)) {
                Files.createDirectories(TEMP_PATH);
                System.out.println("Creado con éxito directorio temporal: " + TEMP_PATH);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Error creando directorio temporal: " + TEMP_PATH);
        }
    }
}
