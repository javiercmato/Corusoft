package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface CustomizedCategoryRepository extends ListCrudRepository<CustomizedCategory, CustomizedCategoryID>,
        ListPagingAndSortingRepository<CustomizedCategory, CustomizedCategoryID> {

}
