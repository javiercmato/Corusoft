package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomizedCategoryRepository extends CrudRepository<CustomizedCategory, CustomizedCategoryID>,
        PagingAndSortingRepository<CustomizedCategory, CustomizedCategoryID> {

}
