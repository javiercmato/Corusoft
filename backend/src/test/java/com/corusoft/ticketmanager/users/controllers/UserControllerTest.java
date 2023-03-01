package com.corusoft.ticketmanager.users.controllers;

import com.corusoft.ticketmanager.common.dtos.ErrorsDTO;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.users.controllers.dtos.*;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.entities.UserRole;
import com.corusoft.ticketmanager.users.exceptions.IncorrectLoginException;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import com.corusoft.ticketmanager.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

import static com.corusoft.ticketmanager.common.security.JwtFilter.*;
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
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Locale locale = Locale.getDefault();
    private final String DEFAULT_NICKNAME = "Foo";
    private final String DEFAULT_PASSWORD = "Bar";

    /* ************************* DEPENDENCIAS ************************* */
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;


    /* ************************* MÉTODOS AUXILIARES ************************* */

    /**
     * Genera datos de un usuario válido.
     */
    private User generateValidUser() {
        User user = new User();
        user.setNickname(DEFAULT_NICKNAME);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setName("TestUser Name");
        user.setEmail(user.getNickname().toLowerCase() + "@corusoft.udc");
        user.setRole(UserRole.USER);
        user.setRegistered_at(LocalDateTime.now());

        return user;
    }

    /**
     * Registra el usuario recibido en el sistema y devuelve sus datos y el token de acceso
     */
    private AuthenticatedUserDTO generateAuthenticatedUser(User user) throws IncorrectLoginException {
        // Guardar al usuario en la BD
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);

        // Generar el DTO con los datos del usuario recién creado
        LoginParamsDTO loginParamsDTO = new LoginParamsDTO();
        loginParamsDTO.setNickname(user.getNickname());
        loginParamsDTO.setPassword(DEFAULT_PASSWORD);

        // Iniciar sesión para obtener los datos del usuario y el token
        return userController.login(loginParamsDTO);
    }

    /**
     * Devuelve el DTO necesario para poder registrar a un usuario en el controlador
     */
    private RegisterUserParamsDTO generateRegisterParamsDtoFromUser(User user) {
        RegisterUserParamsDTO dto = new RegisterUserParamsDTO();
        dto.setNickname(user.getNickname());
        dto.setRawPassword(user.getPassword());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }

    /**
     * Recupera el texto asociado a la propiedad recibida a partir del fichero de I18N en el idioma indicado.
     */
    private String getI18NExceptionMessage(String propertyName, Locale locale) {
        return messageSource.getMessage(
                propertyName,
                null,
                propertyName,
                locale
        );
    }

    /**
     * Recupera el texto asociado a la propiedad recibida con los parámetros recibidos a partir del fichero de I18N en el idioma indicado.
     */
    private String getI18NExceptionMessageWithParams(String propertyName, Locale locale, Object[] args, Class exceptionClass) {
        String exceptionMessage = messageSource.getMessage(
                exceptionClass.getSimpleName(), null, exceptionClass.getSimpleName(), locale
        );
        // Añadir el mensaje traducido al principio del array de argumentos a traducir
        Object[] values = new Object[args.length + 1];
        System.arraycopy(args, 0, values, 1, args.length);
        return messageSource.getMessage(
                propertyName,
                args,
                propertyName,
                locale
        );
    }

    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenRegister_thenUserIsCreated() throws Exception {
        // Crear datos de prueba
        User validUser = generateValidUser();
        RegisterUserParamsDTO paramsDTO = generateRegisterParamsDtoFromUser(validUser);     // Parámetros para registrarse

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
        User validUser = generateValidUser();
        AuthenticatedUserDTO authUserDTO = generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
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
        String errorMessage = getI18NExceptionMessage(UserController.INCORRECT_LOGIN_EXCEPTION_KEY, locale);

        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        loginAction.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(encodedResponseBodyContent));
    }

    @Test
    void whenLoginWithToken_thenReturnAuthenticatedUserDTO() throws Exception {
        // Crear datos de prueba
        User validUser = generateValidUser();
        AuthenticatedUserDTO authUserDTO = generateAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
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

}
