/*package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.*;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateTicketParamsDTO;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.TicketFilterParamsDTO;
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

    Ticket shareTicket(Long userID, Long ticketID, Long receiverID) throws EntityNotFoundException, TicketAlreadySharedException,
            PermissionException;

    void deleteTicket(Long userID, Long ticketID) throws EntityNotFoundException, TicketNotInPropertyException;

    Ticket getTicketDetails(Long userID, Long ticketID) throws EntityNotFoundException, TicketNotInPropertyException;

    List<Ticket> getSharedTickets(Long userId) throws EntityNotFoundException;
    List<Ticket> filterUserTicketsByCriteria(Long userID, TicketFilterParamsDTO params) throws EntityNotFoundException;
}
*/