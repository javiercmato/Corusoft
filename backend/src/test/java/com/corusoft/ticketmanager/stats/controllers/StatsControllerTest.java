package com.corusoft.ticketmanager.stats.controllers;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.stats.services.StatsService;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.users.controllers.dtos.AuthenticatedUserDTO;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.corusoft.ticketmanager.common.security.JwtFilter.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StatsControllerTest {
    /* ************************* CONSTANTES ************************* */
    private static final String API_ENDPOINT = "/api/stats";

    /* ************************* DEPENDENCIAS ************************* */
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private StatsService statsService;
    @Autowired
    private CustomizedCategoryRepository categoryRepository;


    /* ************************* MÉTODOS AUXILIARES ************************* */


    /* ************************* CICLO VIDA TESTS ************************* */
    @BeforeEach
    void beforeEach() {
        // Limpiar datos guardados de otros test
    }


    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenGetSpendingsPerMonth_thenSpendingsPerMonth() throws Exception {

        User user = userRepository.save(testUtils.generateValidUser());
        Category category = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(user, category);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();
        Ticket ticket = testUtils.registerTicket(customizedCategory, user, parsedTicketData);
        Ticket ticket1 = testUtils.registerTicket(customizedCategory, user, parsedTicketData);
        ticket1.setRegisteredAt(LocalDateTime.now().plusMonths(1));
        ticketRepository.save(ticket1);

        AuthenticatedUserDTO authUserDTO = testUtils.generateAuthenticatedUser(user);
        JwtData jwtData = jwtGenerator.extractInfoFromToken(authUserDTO.getServiceToken());

        String endpoint =  "/api/stats/spendingsPerMonth" ;

        ResultActions actions = mockMvc.perform(
                get(endpoint)
                        // Valores anotados como @RequestAttribute
                        .requestAttr(USER_ID_ATTRIBUTE_NAME, jwtData.getUserID())
                        .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, jwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isOk());


    }
}
