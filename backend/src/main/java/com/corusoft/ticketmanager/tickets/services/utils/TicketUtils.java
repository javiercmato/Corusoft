package com.corusoft.ticketmanager.tickets.services.utils;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class TicketUtils {
    @Autowired
    private CategoryRepository categoryRepo;


    /**
     * Busca una categoría por su nombre en la base de datos.
     *
     * @param name Nombre de la categoría a buscar
     * @return Categoría encontrada
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public Category fetchCategoryByName(String name) throws EntityNotFoundException {
        return categoryRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(Category.class.getSimpleName(), name));
    }


}
