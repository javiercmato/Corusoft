package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.exceptions.UnableToParseImageException;
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
}
