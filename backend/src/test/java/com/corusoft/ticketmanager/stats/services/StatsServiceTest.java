package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateTicketParamsDTO;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.CategoryDto;
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

        Map<YearMonth, Double> expected_results = new HashMap<>();
        LocalDateTime modifiedDate;
        List<LocalDateTime> dates = new ArrayList<>();

        // Ejecutar funcionalidades base
        for (int i= 0; i < 3 ; i++) {
            Ticket ticket = testUtils.generateValidTicket(customizedCategory, validUser, parsedTicketData);
            modifiedDate = ticket.getEmittedAt().withHour(0).withMinute(0).withSecond(0)
                    .withNano(0).withDayOfMonth(1).plusMonths(i+1);
            ticket.setEmittedAt(modifiedDate);
            dates.add(modifiedDate);
            ticketrepo.save(ticket);
            expected_results.put(YearMonth.from(modifiedDate), Double.valueOf(ticket.getAmount()));
        }

        // Añadir otro ticket en el més del último
        Ticket ticketLast = testUtils.generateValidTicket(customizedCategory, validUser, parsedTicketData);
        modifiedDate = ticketLast.getEmittedAt().withHour(0).withMinute(0).withSecond(0)
                .withNano(0).withDayOfMonth(1).plusMonths(3);
        ticketLast.setEmittedAt(modifiedDate);
        dates.add(modifiedDate);
        ticketLast.setAmount(3F);
        ticketrepo.save(ticketLast);

        expected_results.remove(YearMonth.from(modifiedDate));
        expected_results.put(YearMonth.from(modifiedDate), 4.0);

        Map<YearMonth, Double> results = statsService.getSpendingsByUser(validUser.getId());

        boolean isEquals = mapEquals(results , expected_results);
        assertTrue(isEquals);

    }

    @Test
    void getSpendindsPerMonthTest() throws Exception {
        // Crear datos de prueba
        User validUser = userRepo.save(testUtils.generateValidUser());
        Category validCategory1 = testUtils.registerValidCategory("c1");
        Category validCategory2 = testUtils.registerValidCategory("c2");
        CustomizedCategory customizedCategory1 = testUtils.registerCustomizedCategory(validUser, validCategory1);
        CustomizedCategory customizedCategory2 = testUtils.registerCustomizedCategory(validUser, validCategory2);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();
        Map<CategoryDto, Double> expectedMap = new HashMap<>();
        // Ejecutar funcionalidades base
        for (int i= 0; i < 4 ; i++) {
            if(i >= 2) {
                Ticket ticket = testUtils.generateValidTicket(customizedCategory1, validUser, parsedTicketData);
                ticketrepo.save(ticket);
            } else {
                Ticket ticket = testUtils.generateValidTicket(customizedCategory2, validUser, parsedTicketData);
                ticket.setAmount(9F);
                ticketrepo.save(ticket);
            }
        }

        expectedMap.put(new CategoryDto(validCategory2.getId(), validCategory2.getName()), 18.0);
        expectedMap.put(new CategoryDto(validCategory1.getId(), validCategory1.getName()), 2.0);

        // Comprobar resultados
        Map<CategoryDto, Double> userSpendingsPerMonth = statsService.getSpendingsThisMonth(validUser.getId());
        boolean isEquals = mapEquals(expectedMap, userSpendingsPerMonth);

        assertTrue(isEquals);
    }

    @Test
    void getPercentagePerCategoryThisMonthTest() throws Exception {
        // Crear datos de prueba
        User validUser = userRepo.save(testUtils.generateValidUser());
        Category validCategory1 = testUtils.registerValidCategory("c1");
        Category validCategory2 = testUtils.registerValidCategory("c2");
        CustomizedCategory customizedCategory1 = testUtils.registerCustomizedCategory(validUser, validCategory1);
        CustomizedCategory customizedCategory2 = testUtils.registerCustomizedCategory(validUser, validCategory2);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();
        Map<CategoryDto, Double> expectedMap = new HashMap<>();
        // Ejecutar funcionalidades base
        for (int i= 0; i < 4 ; i++) {
            if(i >= 2) {
                Ticket ticket = testUtils.generateValidTicket(customizedCategory1, validUser, parsedTicketData);
                ticketrepo.save(ticket);
            } else {
                Ticket ticket = testUtils.generateValidTicket(customizedCategory2, validUser, parsedTicketData);
                ticket.setAmount(9F);
                ticketrepo.save(ticket);
            }
        }

        expectedMap.put(new CategoryDto(validCategory2.getId(), validCategory2.getName()), 90.0);
        expectedMap.put(new CategoryDto(validCategory1.getId(), validCategory1.getName()), 10.0);
        // Comprobar resultados
        Map<CategoryDto, Double> percentageMap = statsService.getPercentagePerCategoryThisMonth(validUser.getId());

        boolean isEquals = mapEquals(expectedMap, percentageMap);
        assertTrue(isEquals);
    }

    @Test
    void getWastedPerCategoryTest() throws Exception {
        // Crear datos de prueba
        User validUser = userRepo.save(testUtils.generateValidUser());
        Category validCategory1 = testUtils.registerValidCategory("c1");
        Category validCategory2 = testUtils.registerValidCategory("c2");
        CustomizedCategory customizedCategory1 = testUtils.registerCustomizedCategory(validUser, validCategory1);
        CustomizedCategory customizedCategory2 = testUtils.registerCustomizedCategory(validUser, validCategory2);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();
        Map<YearMonth, Double> expectedMap = new HashMap<>();
        LocalDateTime modifiedDate;
        List<LocalDateTime> dates = new ArrayList<>();

        for (int i= 0; i < 2 ; i++) {
            Ticket ticket = testUtils.generateValidTicket(customizedCategory2, validUser, parsedTicketData);
            ticketrepo.save(ticket);
        }
        // Ejecutar funcionalidades base
        for (int i= 0; i < 2 ; i++) {
            Ticket ticket = testUtils.generateValidTicket(customizedCategory1, validUser, parsedTicketData);
            modifiedDate = ticket.getEmittedAt().withHour(0).withMinute(0).withSecond(0)
                    .withNano(0).withDayOfMonth(1).plusMonths(i+1);
            ticket.setEmittedAt(modifiedDate);
            ticketrepo.save(ticket);
            dates.add(modifiedDate);
        }

        expectedMap.put(YearMonth.from(dates.get(0)), 1.0);
        expectedMap.put(YearMonth.from(dates.get(1)), 1.0);
        // Comprobar resultados
        Map<YearMonth, Double> percentageMap = statsService.getWastesPerCategory(validUser.getId(), validCategory1.getId());


        boolean isEquals = mapEquals(expectedMap, percentageMap);
        assertTrue(isEquals);
    }
    private static <K, V> boolean mapEquals(Map<K, V> map1, Map<K, V> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }

        for (K key : map1.keySet()) {
            if (!map2.containsKey(key) || !map1.get(key).equals(map2.get(key))) {
                return false;
            }
        }

        return true;
    }

}
