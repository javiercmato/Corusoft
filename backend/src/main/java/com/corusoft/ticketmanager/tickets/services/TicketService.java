package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.users.entities.User;

public interface TicketService {

    CustomizedCategory createCustomCategory(Long userId, String customCategoryName, Float wasteLimit)
            throws EntityAlreadyExistsException, EntityNotFoundException;
}
