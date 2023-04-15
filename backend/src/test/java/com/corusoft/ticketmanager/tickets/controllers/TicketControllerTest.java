package com.corusoft.ticketmanager.tickets.controllers;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.tickets.controllers.dtos.*;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.CategoryConversor;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.TicketConversor;
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

import java.util.Locale;

import static com.corusoft.ticketmanager.common.security.JwtFilter.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TicketControllerTest {
    /* ************************* CONSTANTES ************************* */
    private static final String API_ENDPOINT = "/api/tickets";
    private final Locale locale = Locale.getDefault();


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
    private CustomizedCategoryRepository categoryRepository;


    /* ************************* MÉTODOS AUXILIARES ************************* */


    /* ************************* CICLO VIDA TESTS ************************* */
    @BeforeEach
    void beforeEach() {
        // Limpiar datos guardados de otros test

    }


    /* ************************* CASOS DE PRUEBA ************************* */

    @Test
    void whenCreateCustomizedCategory_thenCustomizedCategoryDTO() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.generateValidUser();
        AuthenticatedUserDTO authUserDTO = testUtils.generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        JwtData jwtData = jwtGenerator.extractInfoFromToken(authUserDTO.getServiceToken());
        Category validCategory = testUtils.registerValidCategory();
        CreateCustomizedCategoryParamsDTO paramsDTO = new CreateCustomizedCategoryParamsDTO();
        paramsDTO.setName(validCategory.getName());
        paramsDTO.setMaxWasteLimit(testUtils.DEFAULT_CATEGORY_MAX_WASTE_LIMIT);

        CustomizedCategoryDTO customizedCategoryDTO = new CustomizedCategoryDTO();
        customizedCategoryDTO.setId(new CustomizedCategoryID(validUser.getId(), validCategory.getId()));
        customizedCategoryDTO.setName(validCategory.getName());
        customizedCategoryDTO.setMaxWasteLimit(testUtils.DEFAULT_CATEGORY_MAX_WASTE_LIMIT);

        // Ejecutar funcionalidades
        String endpoint = API_ENDPOINT + "/categories";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        String expectedContent = this.jsonMapper.writeValueAsString(customizedCategoryDTO);
        ResultActions actions = mockMvc.perform(
            post(endpoint)
                // Valores anotados como @RequestAttribute
                    .requestAttr(USER_ID_ATTRIBUTE_NAME, jwtData.getUserID())
                    .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, jwtData.toString())
                    .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                    .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(encodedBodyContent)
        );

        // Comprobar resultados
        actions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(expectedContent));
    }

    @Test
    void whenUpdateCustomizedCategory_thenCustomizedCategoryDTO() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.generateValidUser();
        // Guardar usuario en BD
        userRepository.save(validUser);
        Category validCategory = testUtils.registerValidCategory();
        AuthenticatedUserDTO authUserDTO = testUtils.generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(validUser, validCategory);
        JwtData jwtData = jwtGenerator.extractInfoFromToken(authUserDTO.getServiceToken());

        UpdateCustomizedCategoryParamsDTO paramsDTO = new UpdateCustomizedCategoryParamsDTO();

        paramsDTO.setMaxWasteLimit(90F);

        // Ejecutar funcionalidades
        String endpoint = API_ENDPOINT + "/categories/" + validCategory.getId().toString();
        CustomizedCategoryDTO customizedCategoryDTO = new CustomizedCategoryDTO();
        customizedCategoryDTO.setMaxWasteLimit(paramsDTO.getMaxWasteLimit());

        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);

        ResultActions actions = mockMvc.perform(
                put(endpoint)
                        // Valores anotados como @RequestAttribute
                        .requestAttr(USER_ID_ATTRIBUTE_NAME, jwtData.getUserID())
                        .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, jwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );

        // Comprobar resultados
        CustomizedCategoryDTO expectedResponse = CategoryConversor.toCustomizedCategoryDTO(customizedCategory);
        String expectedResponseBdoy = this.jsonMapper.writeValueAsString(expectedResponse);
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponseBdoy));
    }

    @Test
    void whenCreateTicket_thenTicketDTO() throws Exception {
        // Crear datos de prueba
        User author = testUtils.generateValidUser();
        AuthenticatedUserDTO authorAuthDTO = testUtils.generateAuthenticatedUser(author);
        JwtData authorJwtData = jwtGenerator.extractInfoFromToken(authorAuthDTO.getServiceToken());
        Category validCategory = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(author, validCategory);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();

        CreateTicketParamsDTO params = testUtils.generateCreateTicketParamsDTO(author, customizedCategory, parsedTicketData);

        // Ejecutar funcionalidades
        String endpoint = API_ENDPOINT + "/";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(params);
        ResultActions actions = mockMvc.perform(
                post(endpoint)
                        // Valores anotados como @RequestAttribute
                        .requestAttr(USER_ID_ATTRIBUTE_NAME, authorJwtData.getUserID())
                        .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, authorJwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authorAuthDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        Ticket expectedResponse = author.getTickets().stream().findFirst().get();
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(TicketConversor.toTicketDTO(expectedResponse));

        // Comprobar resultados
        actions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(encodedResponseBodyContent));
    }

    @Test
    void whenShareTicket_thenTicketDTO() throws Exception {
        // Crear datos de prueba
        User author = testUtils.generateValidUser("author");
        userRepository.save(author);
        User receiver = testUtils.generateValidUser("receiver");
        userRepository.save(receiver);
        Category validCategory = testUtils.registerValidCategory();
        CustomizedCategory customizedCategory = testUtils.registerCustomizedCategory(author, validCategory);
        ParsedTicketData parsedTicketData = testUtils.registerParsedTicketData();
        Ticket ticket = testUtils.registerTicket(customizedCategory, author, parsedTicketData);
        AuthenticatedUserDTO authorAuthDTO = testUtils.generateAuthenticatedUser(author);
        JwtData authorJwtData = jwtGenerator.extractInfoFromToken(authorAuthDTO.getServiceToken());
        ShareTicketParamsDTO params = new ShareTicketParamsDTO(author.getId(), receiver.getId());

        // Ejecutar funcionalidades
        String endpoint = API_ENDPOINT + "/share/" + ticket.getId().toString();
        String encodedBodyContent = this.jsonMapper.writeValueAsString(params);
        ResultActions actions = mockMvc.perform(
                post(endpoint)
                        // Valores anotados como @RequestAttribute
                        .requestAttr(USER_ID_ATTRIBUTE_NAME, authorJwtData.getUserID())
                        .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, authorJwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authorAuthDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        Ticket expectedResponse = ticketRepository.findById(ticket.getId()).get();
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(TicketConversor.toTicketDTO(expectedResponse));

        // Comprobar resultados
       actions
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().string(encodedResponseBodyContent));
    }

}
