package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.repositories.CategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import com.corusoft.ticketmanager.tickets.services.utils.TicketUtils;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CustomizedCategoryRepository customCategoryRepo;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TicketUtils ticketUtils;


    /* ******************** FUNCIONALIDADES CATEGORÍAS ******************** */
    @Override
    public CustomizedCategory createCustomCategory(Long userID, String customCategoryName, Float wasteLimit)
            throws EntityNotFoundException {
        // Comprobar si existe el usuario
        User user = userUtils.fetchUserByID(userID);
        // Comprobar si existe una categoría con nombre recibido
        Category category = ticketUtils.fetchCategoryByName(customCategoryName);

        // Crear CustomCategory con el límite de gasto asignado
        CustomizedCategory customCategory = new CustomizedCategory();
        customCategory.setMaxWasteLimit(wasteLimit);
        customCategory.setCategory(category);
        user.assignCustomizedCategory(customCategory);

        return customCategoryRepo.save(customCategory);
    }


    /* ******************** FUNCIONALIDADES TICKETS ******************** */

}
