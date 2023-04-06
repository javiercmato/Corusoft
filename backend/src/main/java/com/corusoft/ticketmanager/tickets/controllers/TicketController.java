package com.corusoft.ticketmanager.tickets.controllers;

import com.corusoft.ticketmanager.common.dtos.ErrorsDTO;
import com.corusoft.ticketmanager.common.dtos.GenericValueDTO;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.exceptions.UnableToParseImageException;
import com.corusoft.ticketmanager.tickets.controllers.dtos.*;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.CategoryConversor;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.TicketConversor;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private TicketService ticketService;


    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    public static final String UNABLE_TO_PARSE_IMAGE_EXCEPTION_KEY = "tickets.exceptions.UnableToParseImageException";

    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(UnableToParseImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleUnableToParseImageException(UnableToParseImageException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                UNABLE_TO_PARSE_IMAGE_EXCEPTION_KEY, null, UNABLE_TO_PARSE_IMAGE_EXCEPTION_KEY, locale);

        return new ErrorsDTO(errorMessage);
    }

    /* ******************** ENDPOINTS ******************** */
    @GetMapping(path = "/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
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
                                                          @Validated @RequestBody UpdateCustomizedCategoryParamsDTO params)
            throws EntityNotFoundException {

        // Actualizar la categoría customizada
        CustomizedCategory customCategory = ticketService.updateCustomCategory(userID,
                categoryID, params.getMaxWasteLimit());

        // Devolver categoría personalizada
        return CategoryConversor.toCustomizedCategoryDTO(customCategory);
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

    /* ******************** FUNCIONES AUXILIARES ******************** */


}
