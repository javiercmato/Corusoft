package com.corusoft.ticketmanager.users.controllers;

import com.corusoft.ticketmanager.common.dtos.ErrorsDTO;
import com.corusoft.ticketmanager.common.exceptions.*;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.users.controllers.dtos.*;
import com.corusoft.ticketmanager.users.controllers.dtos.conversors.SubscriptionConversor;
import com.corusoft.ticketmanager.users.controllers.dtos.conversors.UserConversor;
import com.corusoft.ticketmanager.users.entities.Subscription;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.exceptions.IncorrectLoginException;
import com.corusoft.ticketmanager.users.exceptions.UserAlreadySubscribedException;
import com.corusoft.ticketmanager.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    public static final String INCORRECT_LOGIN_EXCEPTION_KEY = "users.exceptions.IncorrectLoginException";
    public static final String USER_ALREADY_SUBSCRIBED_EXCEPTION_KEY = "users.exceptions.UserAlreadySubscribedException";


    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(IncorrectLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                INCORRECT_LOGIN_EXCEPTION_KEY, null, INCORRECT_LOGIN_EXCEPTION_KEY, locale
        );

        return new ErrorsDTO(errorMessage);
    }

    @ExceptionHandler(UserAlreadySubscribedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleUserAlreadySubscribedException(UserAlreadySubscribedException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                USER_ALREADY_SUBSCRIBED_EXCEPTION_KEY, null, USER_ALREADY_SUBSCRIBED_EXCEPTION_KEY, locale
        );

        return new ErrorsDTO(errorMessage);
    }


    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthenticatedUserDTO> register(@Validated @RequestBody RegisterUserParamsDTO params)
            throws EntityAlreadyExistsException {
        // Parsear datos recibidos
        User parsedUser = UserConversor.fromRegisterUserParamsDTO(params);

        // Registrar usuario en servicio
        User registeredUser = userService.signUp(parsedUser);

        // Generar contenido de la respuesta
        URI resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userID}")
                .buildAndExpand(registeredUser.getId())
                .toUri();
        String serviceToken = this.generateServiceTokenFromUser(registeredUser);
        AuthenticatedUserDTO authUserDTO = UserConversor.toAuthenticatedUserDTO(registeredUser, serviceToken);

        // Crear respuesta HTTP y enviarla
        return ResponseEntity
                .created(resourceLocation)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authUserDTO);
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticatedUserDTO login(@Validated @RequestBody LoginParamsDTO params)
            throws IncorrectLoginException {
        // Inicia sesión en el servicio
        User user = userService.login(params.getNickname(), params.getPassword());

        // Generar token para usuario
        String token = generateServiceTokenFromUser(user);

        // Devolver datos de usuario junto al token generado
        return UserConversor.toAuthenticatedUserDTO(user, token);
    }

    @PostMapping(path = "/login/token",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticatedUserDTO loginUsingToken(@RequestAttribute("userID") Long userID, @RequestAttribute("serviceToken") String token)
            throws EntityNotFoundException {
        // Inicia sesión en el servicio
        User user = userService.loginFromToken(userID);

        // Devuelve los datos del usuario junto al token recibido
        return UserConversor.toAuthenticatedUserDTO(user, token);
    }

    @PostMapping(path = "/subscribe/{userID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SubscriptionDTO subscribeToPremium(@RequestAttribute("userID") Long userID, @PathVariable("userID") Long pathUserID)
            throws PermissionException, EntityNotFoundException, UserAlreadySubscribedException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!this.doUsersMatch(userID, pathUserID))
            throw new PermissionException();

        // Crear subscripción
        Subscription subscription = userService.subscribeToPremium(userID);

        // Devolver subscripción
        return SubscriptionConversor.toSubscriptionDTO(subscription);
    }

    /* ******************** FUNCIONES AUXILIARES ******************** */

    /**
     * Genera un JWT para el usuario recibido
     */
    public String generateServiceTokenFromUser(User user) {
        JwtData jwtData =
                new JwtData(user.getId(), user.getNickname(), user.getRole().toString());

        return jwtGenerator.generateJWT(jwtData);
    }

    /**
     * Comprueba si dos usuarios son el mismo comparando sus ID
     */
    public boolean doUsersMatch(Long requestUserID, Long targetUserID) {
        return requestUserID.equals(targetUserID);
    }

}
