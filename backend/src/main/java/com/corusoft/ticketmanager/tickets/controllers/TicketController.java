package com.corusoft.ticketmanager.tickets.controllers;

import com.corusoft.ticketmanager.common.dtos.ErrorsDTO;
import com.corusoft.ticketmanager.common.dtos.GenericValueDTO;
import com.corusoft.ticketmanager.common.exceptions.*;
import com.corusoft.ticketmanager.tickets.controllers.dtos.*;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.CategoryConversor;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.TicketConversor;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.TicketFilterParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TicketService ticketService;


    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    public static final String UNABLE_TO_PARSE_IMAGE_EXCEPTION_KEY = "tickets.exceptions.UnableToParseImageException";
    public static final String TICKET_ALREADY_SHARED_EXCEPTION_KEY = "tickets.exceptions.TicketAlreadySharedException";
    public static final String TICKET_NOT_IN_PROPERTY_EXCEPTION_KEY = "tickets.exceptions.TicketNotInPropertyException";

    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(UnableToParseImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleUnableToParseImageException(UnableToParseImageException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                UNABLE_TO_PARSE_IMAGE_EXCEPTION_KEY, null, UNABLE_TO_PARSE_IMAGE_EXCEPTION_KEY, locale);

        return new ErrorsDTO(errorMessage);
    }

    @ExceptionHandler(TicketAlreadySharedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleTicketAlreadySharedException(TicketAlreadySharedException exception, Locale locale) {
        String exceptionMessage = messageSource.getMessage(
                exception.getEntityName(), null, exception.getEntityName(), locale
        );

        String globalErrorMessage = messageSource.getMessage(
                TICKET_ALREADY_SHARED_EXCEPTION_KEY,
                new Object[]{exceptionMessage, exception.getKey().toString()},
                TICKET_ALREADY_SHARED_EXCEPTION_KEY,
                locale
        );

        return new ErrorsDTO(globalErrorMessage);
    }

    @ExceptionHandler(TicketNotInPropertyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)     // 401
    @ResponseBody
    public ErrorsDTO handleTicketNotInPropertyException(TicketNotInPropertyException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                TICKET_NOT_IN_PROPERTY_EXCEPTION_KEY, null, TICKET_NOT_IN_PROPERTY_EXCEPTION_KEY, locale);

        return new ErrorsDTO(errorMessage);
    }

    /* ******************** ENDPOINTS ******************** */
    @GetMapping(path = "/categories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        return ticketService.getAllCategories();
    }

    @PostMapping(path = "/categories",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CustomizedCategoryDTO createCustomizedCategory(@RequestAttribute("userID") Long userID,
                                                          @Validated @RequestBody CreateCustomizedCategoryParamsDTO params)
            throws EntityNotFoundException {
        // Crear categoría personalizada
        CustomizedCategory customCategory = ticketService.createCustomCategory(userID, params.getName(), params.getMaxWasteLimit());

        // Devolver categoría personalizada
        return CategoryConversor.toCustomizedCategoryDTO(customCategory);
    }

    @PutMapping(path = "/categories/{categoryID}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CustomizedCategoryDTO updateCustomizedCategory(@RequestAttribute("userID") Long userID,
                                                          @PathVariable("categoryID") Long categoryID,
                                                          @Validated @RequestBody GenericValueDTO<Float> params)
            throws EntityNotFoundException {
        // Actualizar la categoría personalizada
        CustomizedCategory customCategory = ticketService.updateCustomCategory(userID, categoryID, params.getValue());

        // Devolver categoría personalizada
        return CategoryConversor.toCustomizedCategoryDTO(customCategory);
    }

    @GetMapping(path = "/categories/{userID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CustomizedCategoryDTO> getCustomizedCategoriesByUser(@RequestAttribute("userID") Long userID,
                                                                     @PathVariable("userID") Long pathUserID)
            throws PermissionException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario que solicita la operación son el mismo
        if (!userUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();

        // Recuperar categorías personalizadas del usuario
        List<CustomizedCategory> customCategories = ticketService.getCustomCategoriesByUser(userID);

        return CategoryConversor.toCustomizedCategoryDTOList(customCategories);
    }

    @PostMapping(path = "/parse",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ParsedTicketDTO parseTicket(@RequestBody GenericValueDTO<String> params) throws UnableToParseImageException {
        // Parsear ticket (recibido como una imágen codificada como String en Base64)
        String imageB64String = params.getValue();
        ParsedTicketData parsedTicket = ticketService.parseTicketContent(imageB64String);

        return TicketConversor.toParsedTicketDTO(parsedTicket);
    }

    @PostMapping(path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TicketDTO createTicket(@RequestAttribute("userID") Long userID,
                                  @Validated @RequestBody CreateTicketParamsDTO params)
            throws EntityNotFoundException, UnableToParseImageException, PermissionException {
        // Comprobar que el usuario actual y el usuario que solicita la operación son el mismo
        if (!userUtils.doUsersMatch(userID, params.getUserID()))
            throw new PermissionException();

        // Crear ticket
        Ticket createdTicket = ticketService.createTicket(params);

        return TicketConversor.toTicketDTO(createdTicket);
    }

    @PostMapping(path = "/share/{ticketID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TicketDTO shareTicket(@RequestAttribute("userID") Long userID,
                                 @PathVariable("ticketID") Long ticketID,
                                 @Validated @RequestBody ShareTicketParamsDTO params)
            throws EntityNotFoundException, TicketAlreadySharedException, PermissionException {
        // Comprobar que el usuario actual y el usuario que solicita la operación son el mismo
        if (!userUtils.doUsersMatch(userID, params.getSenderID()))
            throw new PermissionException();

        // Comprobar que usuario no intenta compartir ticket consigo mismo
        if (userUtils.doUsersMatch(params.getSenderID(), params.getReceiverID()))
            throw new PermissionException();

        Ticket ticket = ticketService.shareTicket(userID, ticketID, params.getReceiverID());

        return TicketConversor.toTicketDTO(ticket);
    }

    @DeleteMapping(path = "/{ticketID}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResponseEntity<Void> deleteTicket(@RequestAttribute("userID") Long userID,
                                             @PathVariable("ticketID") Long ticketID)
            throws EntityNotFoundException, TicketNotInPropertyException {
        // Llamada al servicio
        ticketService.deleteTicket(userID, ticketID);

        // Generar respuesta
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{ticketID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public TicketDTO getTicketDetails(@RequestAttribute("userID") Long userID,
                                      @PathVariable("ticketID") Long ticketID) throws TicketNotInPropertyException, EntityNotFoundException {
        // Llamada al servicio
        Ticket ticket = ticketService.getTicketDetails(userID, ticketID);

        return TicketConversor.toTicketDTO(ticket);
    }

    @PutMapping(path = "/{userID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TicketDTO> filterUserTicketsByCriteria(@RequestAttribute("userID") Long userID,
                                                       @PathVariable("userID") Long pathUserID,
                                                       @Validated @RequestBody TicketFilterParamsDTO params)
            throws PermissionException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario que solicita la operación son el mismo
        if (!userUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();

        // Llamada al servicio
        List<Ticket> tickets = ticketService.filterUserTicketsByCriteria(userID, params);

        return TicketConversor.toTicketDTOList(tickets);
    }

    /* ******************** FUNCIONES AUXILIARES ******************** */


}
