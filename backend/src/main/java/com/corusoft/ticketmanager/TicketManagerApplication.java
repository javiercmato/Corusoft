package com.corusoft.ticketmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TicketManagerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TicketManagerApplication.class);
        application.run();
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

}
