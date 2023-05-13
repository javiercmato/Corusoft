package com.corusoft.ticketmanager.users.controllers;

import com.corusoft.ticketmanager.TestUtils;
import com.corusoft.ticketmanager.common.dtos.ErrorsDTO;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.users.controllers.dtos.*;
import com.corusoft.ticketmanager.users.controllers.dtos.conversors.SubscriptionConversor;
import com.corusoft.ticketmanager.users.controllers.dtos.conversors.UserConversor;
import com.corusoft.ticketmanager.users.entities.Subscription;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import com.corusoft.ticketmanager.users.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {
    /* ************************* CONSTANTES ************************* */
    private static final String API_ENDPOINT = "/api/users";

    private final Locale locale = Locale.getDefault();
    private final String DEFAULT_NICKNAME = "Foo";
    private final String DEFAULT_PASSWORD = "Bar";

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
    private UserService userService;



    /* ************************* MÉTODOS AUXILIARES ************************* */

    /* ************************* CICLO VIDA TESTS ************************* */
    @BeforeEach
    void beforeEach() {
        // Limpiar datos guardados de otros test
        userRepository.deleteAll();
    }

    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenRegister_thenUserIsCreated() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.generateValidUser();
        RegisterUserParamsDTO paramsDTO = testUtils.generateRegisterParamsDtoFromUser(validUser);     // Parámetros para registrarse

        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/register";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions signUpAction = mockMvc.perform(
                post(endpointAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );

        // Comprobar resultados
        //AuthenticatedUserDTO expectedDTO = generateAuthenticatedUser(validUser);      // Respuesta deseada
        //String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedDTO);
        signUpAction
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // FIXME: Ver como evitar comparar contraseña sin cifrar al registrar usuario (RegisterUserParamsDTO)
        // y contraseña cifrada al iniciar sesión (AuthenticatedUserDTO)
        //    .andExpect(content().string(encodedResponseBodyContent));
    }

    @Test
    void whenLogin_thenReturnAuthenticatedUserDTO() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.generateValidUser();
        AuthenticatedUserDTO authUserDTO = testUtils.generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        LoginParamsDTO paramsDTO = new LoginParamsDTO(DEFAULT_NICKNAME, DEFAULT_PASSWORD);

        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );

        // Comprobar resultados
        //String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(authUserDTO);
        loginAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //.andExpect(content().string(encodedResponseBodyContent));
    }

    @Test
    void whenLoginNonExistentUser_thenBadRequest_becauseIncorrectLoginException() throws Exception {
        // Crear datos de prueba
        LoginParamsDTO paramsDTO = new LoginParamsDTO(DEFAULT_NICKNAME, DEFAULT_PASSWORD);

        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        String errorMessage = testUtils.getI18NExceptionMessage(UserController.INCORRECT_LOGIN_EXCEPTION_KEY, locale);

        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        loginAction.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(encodedResponseBodyContent));
    }

    @Test
    void whenLoginWithToken_thenReturnAuthenticatedUserDTO() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.generateValidUser();
        AuthenticatedUserDTO authUserDTO = testUtils.generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        JwtData jwtData = jwtGenerator.extractInfoFromToken(authUserDTO.getServiceToken());

        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login/token";
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        // Valores anotados como @RequestAttribute
                        .requestAttr(USER_ID_ATTRIBUTE_NAME, jwtData.getUserID())
                        .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, jwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())

        );

        // Comprobar resultados
        //String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(authUserDTO);
        loginAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //.andExpect(content().string(encodedResponseBodyContent));
    }

    @Test
    void whenSubscribeToPremium_thenReturnSubscriptionDTO() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.generateValidUser();
        AuthenticatedUserDTO authUserDTO = testUtils.generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        JwtData jwtData = jwtGenerator.extractInfoFromToken(authUserDTO.getServiceToken());

        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/subscribe/" + authUserDTO.getUserDTO().getUserID();
        ResultActions subscribeAction = mockMvc.perform(
                post(endpointAddress)
                        // Valores anotados como @RequestAttribute
                        .requestAttr(USER_ID_ATTRIBUTE_NAME, jwtData.getUserID())
                        .requestAttr(SERVICE_TOKEN_ATTRIBUTE_NAME, jwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
        );

        // Comprobar resultados
        Subscription subscription = validUser.getSubscriptions().stream().findFirst().get();
        SubscriptionDTO expectedSubscriptionDTO = SubscriptionConversor.toSubscriptionDTO(subscription);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedSubscriptionDTO);
        subscribeAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(encodedResponseBodyContent));
    }

    @Test
    void whenFindUserByNameOrNickname_thenUserDTO() throws Exception {
        // Crear datos de prueba
        User validUser = testUtils.registerValidUser();

        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT;
        ResultActions subscribeAction = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .queryParam("name", validUser.getName())
        );

        // Comprobar resultados
        UserDTO expectedResponse = UserConversor.toUserDTO(validUser);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        subscribeAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(encodedResponseBodyContent));
    }

}
