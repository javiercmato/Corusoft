package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StatsServiceTest {
    /* ************************* CONSTANTES ************************* */


    /* ************************* DEPENDENCIAS ************************* */
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CustomizedCategoryRepository categoryRepository;
    @Autowired
    private StatsService statsService;


    /* ************************* MÉTODOS AUXILIARES ************************* */


    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenGetSpendingsByUser_thenOK() throws EntityNotFoundException {
        // Crear datos de prueba
        User validUser = userRepository.save(testUtils.generateValidUser());
        Category validCategory = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(validUser, validCategory);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();

        // Ejecutar funcionalidades
        List<Ticket> ticketsList = new ArrayList<>(3);
        for (int i= 0; i < 3 ; i++) {
            Ticket ticket = testUtils.generateValidTicket(customizedCategory, validUser, parsedTicketData);
            LocalDateTime modifiedDate = ticket.getEmittedAt().withMonth(i+1);
            ticket.setEmittedAt(modifiedDate);
            ticketsList.add(ticket);
            ticketRepository.save(ticket);
        }



        Map<YearMonth, Double> results = statsService.getSpendingsByUser(validUser.getId());
        System.out.println(results);

    }

    @Test
    void getSpendindsPerMonthsTest() {
        // Crear datos de prueba
        User validUser = userRepository.save(testUtils.generateValidUser());
        Category validCategory = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(validUser, validCategory);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();

        // Ejecutar funcionalidades
        List<Ticket> ticketsList = new ArrayList<>(12);
        for (Month month : Month.values()) {
            Ticket ticket = testUtils.generateValidTicket(customizedCategory, validUser, parsedTicketData);
            LocalDateTime modifiedDate = ticket.getEmittedAt().withMonth(month.getValue());
            ticket.setEmittedAt(modifiedDate);
            ticketsList.add(ticket);
        }
        ticketRepository.saveAll(ticketsList);

        // Comprobar resultados
        assertAll(
                // Hay un ticket por cada mes
                () -> assertEquals(ticketsList.size(), Month.values().length)
        );
    }

}
