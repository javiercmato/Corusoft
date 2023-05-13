package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateTicketParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
    private UserRepository userRepo;
    @Autowired
    private TicketRepository ticketrepo;
    @Autowired
    private CustomizedCategoryRepository customCategoryRepo;
    @Autowired
    private StatsService statsService;
    @Autowired
    private TicketService ticketService;


    /* ************************* MÉTODOS AUXILIARES ************************* */


    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenGetSpendingsByUser_thenOK() throws EntityNotFoundException {
        // Crear datos de prueba
        User validUser = userRepo.save(testUtils.generateValidUser());
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
            ticketrepo.save(ticket);
        }



        Map<YearMonth, Double> results = statsService.getSpendingsByUser(validUser.getId());
        System.out.println(results);

    }

    @Test
    void getSpendindsPerMonthTest() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.registerValidUser();
        Category validCategory = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(validUser, validCategory);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();

        // Ejecutar funcionalidades
        List<Ticket> ticketsList = new ArrayList<>(Month.values().length);
        for (Month month : Month.values()) {
            // Generar el mismo ticket para cada mes
            Ticket ticket = testUtils.generateValidTicket(customizedCategory, validUser, parsedTicketData);
            LocalDateTime modifiedDate = ticket.getEmittedAt().withMonth(month.getValue());
            ticket.setEmittedAt(modifiedDate);
            CreateTicketParamsDTO paramsDTO = testUtils.parseTicketToCreateTicketParamsDTO(ticket);
            paramsDTO.setTicketData(testUtils.loadImageFromResourcesAsB64String(testUtils.DEFAULT_TEST_TICKET_BASE64_NAME));
            ticket = ticketService.createTicket(paramsDTO);
            ticketsList.add(ticket);
        }

        // Comprobar resultados
        Map<YearMonth, Double> userSpendingsPerMonth = statsService.getSpendingsByUser(validUser.getId());
        assertAll(
                // Se ha creado un ticket para cada mes
                () -> assertEquals(ticketsList.size(), Month.values().length),
                // Hay registrados al menos un ticket por cada mes
                () -> assertEquals(userSpendingsPerMonth.size(), Month.values().length),
                // Todos los tickets valen lo mismo
                () -> assertTrue(userSpendingsPerMonth.values().stream()
                        .allMatch(ammount -> ammount.equals((double) testUtils.DEFAULT_TICKET_AMMOUNT))
                )
        );
    }

}
