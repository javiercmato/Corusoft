package com.corusoft.ticketmanager;

import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.repositories.CategoryRepository;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import com.corusoft.ticketmanager.users.controllers.UserController;
import com.corusoft.ticketmanager.users.controllers.dtos.*;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.entities.UserRole;
import com.corusoft.ticketmanager.users.exceptions.IncorrectLoginException;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import com.corusoft.ticketmanager.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class TestUtils {

    /* ************************* CONSTANTES ************************* */
    public final Locale locale = Locale.getDefault();
    public final String DEFAULT_NICKNAME = "Foo";
    public final String DEFAULT_PASSWORD = "Bar";
    public final String DEFAULT_CATEGORY_NAME = "Category";
    public final Float DEFAULT_CATEGORY_MAX_WASTE_LIMIT = 100f;

    /* ************************* DEPENDENCIAS ************************* */
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private CategoryRepository categoryRepo;


    /* ************************* MÉTODOS AUXILIARES ************************* */

    /**
     * Genera datos de un usuario válido.
     */
    public User generateValidUser() {
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
    public AuthenticatedUserDTO generateAuthenticatedUser(User user) throws IncorrectLoginException {
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
    public RegisterUserParamsDTO generateRegisterParamsDtoFromUser(User user) {
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
    public String getI18NExceptionMessage(String propertyName, Locale locale) {
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
    public String getI18NExceptionMessageWithParams(String propertyName, Locale locale, Object[] args, Class exceptionClass) {
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


    /** Registra una categoría válida */
    public Category registerValidCategory() {
        Category category = new Category();
        category.setName(DEFAULT_CATEGORY_NAME);

        return categoryRepo.save(category);
    }

    /** Genera datos de una categoría customizada válida. */
    public CustomizedCategory generateCustomizedCategory(User user, Category category) {
        CustomizedCategory customCategory = new CustomizedCategory();
        customCategory.setMaxWasteLimit(DEFAULT_CATEGORY_MAX_WASTE_LIMIT);
        customCategory.setCategory(category);
        customCategory.setUser(user);

        return customCategory;
    }

}
