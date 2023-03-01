package com.corusoft.ticketmanager.users.controllers;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.jwt.JwtData;
import com.corusoft.ticketmanager.common.jwt.JwtGenerator;
import com.corusoft.ticketmanager.users.controllers.dtos.AuthenticatedUserDTO;
import com.corusoft.ticketmanager.users.controllers.dtos.RegisterUserParamsDTO;
import com.corusoft.ticketmanager.users.controllers.dtos.conversors.UserConversor;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserService userService;

    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */


    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */


    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthenticatedUserDTO> register(@Validated @RequestBody RegisterUserParamsDTO params) throws EntityAlreadyExistsException {
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

    /* ******************** FUNCIONES AUXILIARES ******************** */

    /**
     * Genera un JWT para el usuario recibido
     */
    public String generateServiceTokenFromUser(User user) {
        JwtData jwtData =
                new JwtData(user.getId(), user.getNickname(), user.getRole().toString());

        return jwtGenerator.generateJWT(jwtData);
    }

}
