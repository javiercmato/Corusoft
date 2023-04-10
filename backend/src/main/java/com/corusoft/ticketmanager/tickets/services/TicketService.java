package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.exceptions.PermissionException;
import com.corusoft.ticketmanager.common.exceptions.TicketAlreadySharedException;
import com.corusoft.ticketmanager.common.exceptions.UnableToParseImageException;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateTicketParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.*;

import java.util.List;

public interface TicketService {

    List<Category> getAllCategories();

    CustomizedCategory createCustomCategory(Long userID, String customCategoryName, Float wasteLimit)
            throws EntityNotFoundException;

    CustomizedCategory updateCustomCategory(Long userID, Long customCategoryID, Float newWasteLimit)
            throws EntityNotFoundException;

    List<CustomizedCategory> getCustomCategoriesByUser(Long userID) throws EntityNotFoundException;

    ParsedTicketData parseTicketContent(String ticketContentAsB64) throws UnableToParseImageException;

    Ticket createTicket(CreateTicketParamsDTO params) throws EntityNotFoundException, UnableToParseImageException;

    void shareTicket(Long userId, Long ticketId, String receiverName) throws EntityNotFoundException, TicketAlreadySharedException,
            PermissionException;
}
