package com.corusoft.ticketmanager.services;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.controllers.dtos.SpendingPerMonthsDTO;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.entities.ParsedTicketData;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import com.corusoft.ticketmanager.tickets.services.utils.Spendings;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TicketServiceTest {


    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils testUtils;

    @Test
    void userSpendingsPerMonthTests() throws EntityNotFoundException {

        User user = userRepository.save(testUtils.generateValidUser());
        Category category = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(user, category);
        ParsedTicketData parsedTicketData = testUtils.registerParseTicket();
        Ticket ticket = testUtils.registerTicket(customizedCategory, user, parsedTicketData);
        Ticket ticket1 = testUtils.registerTicket(customizedCategory, user, parsedTicketData);
        ticket1.setRegisteredAt(LocalDateTime.now().plusMonths(1));
        ticketRepository.save(ticket1);

        List<SpendingPerMonthsDTO> spendingPerMonthsDTOS = testUtils.getSpendingsPerMonth(user.getId());

        List<SpendingPerMonthsDTO> expectedSpendingsPerMonth = new ArrayList<>();
        expectedSpendingsPerMonth.add(new SpendingPerMonthsDTO(
                Month.of(ticket1.getRegisteredAt().getMonthValue()).toString(), ticket1.getAmount()));
        expectedSpendingsPerMonth.add(new SpendingPerMonthsDTO(
                Month.of(ticket.getRegisteredAt().getMonthValue()).toString(), ticket.getAmount()));

        assertEquals(spendingPerMonthsDTOS, expectedSpendingsPerMonth);


    }
}
