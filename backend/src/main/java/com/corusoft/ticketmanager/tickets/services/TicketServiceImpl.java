package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.repositories.CategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class TicketServiceImpl implements TicketService{

    @Autowired
    private CustomizedCategoryRepository customizedCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserUtils userUtils;

    @Override
    public CustomizedCategory createCustomCategory(Long userId, String customCategoryName, Float wasteLimit)
            throws EntityAlreadyExistsException, EntityNotFoundException {

        User expectedUser = userUtils.fetchUserByID(userId);

        if(customizedCategoryRepository.findByName(customCategoryName).isPresent()) {
            throw new EntityAlreadyExistsException(CustomizedCategory.class.getSimpleName(), customCategoryName);
        }

        Optional<Category> categoryOptional = categoryRepository.findByName(customCategoryName);

        CustomizedCategory customizedCategory = new CustomizedCategory();
        customizedCategory.setName(customCategoryName);
        customizedCategory.setUser(expectedUser);
        customizedCategory.setMaxWasteLimit(wasteLimit);

        if (categoryOptional.isPresent()) {
            customizedCategory.setCategory(categoryOptional.get());
        }

        return customizedCategoryRepository.save(customizedCategory);

    }

}
