package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.Category;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface CategoryRepository extends ListCrudRepository<Category, Long>,
        ListPagingAndSortingRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

}
