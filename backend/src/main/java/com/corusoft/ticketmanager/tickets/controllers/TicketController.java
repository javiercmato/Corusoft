package com.corusoft.ticketmanager.tickets.controllers;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateCustomizedCategoryParamsDTO;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CustomizedCategoryDTO;
import com.corusoft.ticketmanager.tickets.controllers.dtos.conversors.CategoryConversor;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private TicketService ticketService;


    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */


    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */


    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/categories",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CustomizedCategoryDTO createCustomizedCategory(@RequestAttribute("userID") Long userID,
                                                          @Validated @RequestBody CreateCustomizedCategoryParamsDTO params)
            throws EntityNotFoundException {
        // Crear categoría personalizada
        CustomizedCategory customCategory = ticketService.createCustomCategory(userID, params.getName(), params.getMaxWasteLimit());

        // Devolver categoría personalizada
        return CategoryConversor.toCustomizedCategoryDTO(customCategory);
    }

    /* ******************** FUNCIONES AUXILIARES ******************** */


}
