package com.corusoft.ticketmanager;

import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateTicketParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.repositories.*;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import com.corusoft.ticketmanager.tickets.services.utils.TicketUtils;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.Locale;

@Component
public class TestUtils {

    /* ************************* CONSTANTES ************************* */
    public final String DEFAULT_NICKNAME = "Foo";
    public final String DEFAULT_PASSWORD = "Bar";
    public final String DEFAULT_CATEGORY_NAME = "Category";
    public final Float DEFAULT_CATEGORY_MAX_WASTE_LIMIT = 100f;
    public final String TESTS_ASSETS_PATH = "src/test/resources/assets/";
    public final String DEFAULT_TEST_TICKET_NAME = "ticket-test.jpeg";
    public final String DEFAULT_TEST_TICKET_BASE64_NAME = "ticket-test-base64string.txt";

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
    @Autowired
    private CustomizedCategoryRepository customCategoryRepo;
    @Autowired
    private TicketRepository ticketRepo;
    @Autowired
    private ParsedTicketDataRepository parsedTicketDataRepo;
    @Autowired
    private TicketUtils ticketUtils;


    /* ************************* MÉTODOS AUXILIARES ************************* */

    /**
     * Genera datos de un usuario válido.
     */
    public User generateValidUser() {
        return this.generateValidUser(DEFAULT_NICKNAME);
    }

    /**
     * Genera datos de un usuario válido con el nickname recibido
     */
    public User generateValidUser(String nickname) {
        nickname = nickname.trim();
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setName("name_" + nickname);
        user.setEmail(nickname.toLowerCase() + "@corusoft.udc");
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
        return this.registerValidCategory(DEFAULT_CATEGORY_NAME);
    }

    /** Registra una categoría válida */
    public Category registerValidCategory(String name) {
        Category category = new Category();
        category.setName(name);

        return categoryRepo.save(category);
    }

    /** Genera datos de una categoría customizada válida. */
    public CustomizedCategory registerCustomizedCategory(User user, Category category) {
        CustomizedCategory customCategory = new CustomizedCategory();
        customCategory.setMaxWasteLimit(DEFAULT_CATEGORY_MAX_WASTE_LIMIT);
        customCategory.setCategory(category);
        customCategory.setUser(user);
        user.assignCustomizedCategory(customCategory);

        return customCategoryRepo.save(customCategory);
    }

    /** Carga una imágen como array de bytes desde la carpeta de test/resources/assets */
    public byte[] loadImageFromResources(String resourceName) {
        Path imagePath = Paths.get(TESTS_ASSETS_PATH, resourceName);
        File imageFile = imagePath.toFile();
        byte[] imageBytes;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bi = ImageIO.read(imageFile);
            ImageIO.write(bi, resourceName, baos);
            imageBytes = baos.toByteArray();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            imageBytes = new byte[0];
        }

        return imageBytes;
    }

    public String loadImageFromResourcesAsB64String(String resourceName) {
        Path imagePath = Paths.get(TESTS_ASSETS_PATH, resourceName);
        String imageB64DataString = "";

        try {
            imageB64DataString = new String(Files.readAllBytes(imagePath));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return imageB64DataString;
    }

    /** Registra en base de datos un ticket */
    public Ticket generateValidTicket(CustomizedCategory customizedCategory, User user, ParsedTicketData parsedTicketData) {
        byte[] ticketPicture = loadImageFromResources(DEFAULT_TEST_TICKET_NAME);

        return Ticket.builder()
                .name(DEFAULT_TEST_TICKET_NAME)
                .customizedCategory(customizedCategory)
                .amount(1.00F)
                .currency("Currency")
                .picture(ticketPicture)
                .store("store")
                .creator(user)
                .parsedTicketData(parsedTicketData)
                .emittedAt(LocalDateTime.now())
                .registeredAt(LocalDateTime.now())
                .build();
    }

    /** Registra en base de datos un ticket */
    public Ticket registerTicket(CustomizedCategory customizedCategory, User user, ParsedTicketData parsedTicketData) {
        byte[] ticketPicture = loadImageFromResources(DEFAULT_TEST_TICKET_NAME);

        Ticket ticket = Ticket.builder()
                .name(DEFAULT_TEST_TICKET_NAME)
                .customizedCategory(customizedCategory)
                .amount(1.00F)
                .currency("Currency")
                .picture(ticketPicture)
                .store("store")
                .creator(user)
                .parsedTicketData(parsedTicketData)
                .emittedAt(LocalDateTime.now())
                .registeredAt(LocalDateTime.now())
                .build();

        return ticketRepo.save(ticket);
    }

    /** Registra en base de datos un ParsedTicketData con datos reales */
    public ParsedTicketData registerParsedTicketData() {
        ParsedTicketData parsedTicketData = ParsedTicketData.builder()
                // Datos reales de un ticket
                .category("food")
                .emitted_at_date(LocalDate.of(2023, Month.FEBRUARY, 22))
                .emitted_at_time("19:20")
                .country("ES")
                .currency("EUR")
                .language("es")
                .subcategory("shopping")
                .supplier("EROSKI CENTER")
                .totalAmount(12.22F)
                .totalTax(0.99F)
                .registered_at(LocalDateTime.now())
                .build();

        return parsedTicketDataRepo.save(parsedTicketData);

    }

    /** Genera un DTO para crear un nuevo ticket */
    public CreateTicketParamsDTO generateCreateTicketParamsDTO(User author, CustomizedCategory customCategory, ParsedTicketData data) {
        String imageB64String = loadImageFromResourcesAsB64String(DEFAULT_TEST_TICKET_BASE64_NAME);

        return CreateTicketParamsDTO.builder()
                .userID(author.getId())
                .supplier(data.getSupplier())
                .categoryID(customCategory.getId().getCategoryID())
                .emmitedAtDate(data.getEmitted_at_date())
                .emmitedAtTime(data.getEmitted_at_time())
                .country(data.getCountry())
                .currency(data.getCurrency())
                .totalTax(data.getTotalTax())
                .totalAmount(data.getTotalAmount())
                .ticketData(imageB64String)
                .name(DEFAULT_TEST_TICKET_NAME)
                .build();
    }

}
