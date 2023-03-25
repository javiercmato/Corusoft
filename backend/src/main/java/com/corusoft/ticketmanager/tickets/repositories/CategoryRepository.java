package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long>,
        PagingAndSortingRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

}
