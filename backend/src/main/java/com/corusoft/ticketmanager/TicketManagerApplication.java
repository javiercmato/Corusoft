package com.corusoft.ticketmanager;

import com.corusoft.ticketmanager.common.ContextStartedEventListener;
import com.mindee.MindeeClient;
import com.mindee.MindeeClientInit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.file.Path;

@SpringBootApplication
public class TicketManagerApplication {
    @Value("${project.mindee.apiKey}")
    private String mindeeApiKey;

    public static final Path TEMP_PATH = Path.of("./backend/temp").toAbsolutePath();

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TicketManagerApplication.class);
        application.addListeners(
            new ContextStartedEventListener()
        );
        application.run(args);
    }

    /* ******************** BEANS GLOBALES ******************** */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:i18n/messages");
        bean.setDefaultEncoding("UTF-8");

        return bean;
    }

    @Bean
    public MindeeClient mindeeClient() {
        return MindeeClientInit.create(mindeeApiKey);
    }
}
