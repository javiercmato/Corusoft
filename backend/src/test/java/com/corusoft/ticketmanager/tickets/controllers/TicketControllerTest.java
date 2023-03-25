package com.corusoft.ticketmanager.tickets.controllers;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateCustomizedCategoryParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.users.controllers.dtos.AuthenticatedUserDTO;
import com.corusoft.ticketmanager.users.entities.User;
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

import static com.corusoft.ticketmanager.common.security.JwtFilter.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


    /* ************************* DEPENDENCIAS ************************* */
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private JwtGenerator jwtGenerator;


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

        // Ejecutar funcionalidades
        String endpoint = API_ENDPOINT + "/categories";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions actions = mockMvc.perform(
            post(endpoint)
                // Valores anotados como @RequestAttribute
                    .requestAttr(USER_ID_ATTRIBUTE_NAME, jwtData.getUserID())
                    .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, jwtData.toString())
                    .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(encodedBodyContent)
        );

        // Comprobar resultados
        actions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(encodedBodyContent));
    }
}
