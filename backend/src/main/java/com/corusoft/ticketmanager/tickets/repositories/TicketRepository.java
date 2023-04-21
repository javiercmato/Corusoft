package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface TicketRepository extends ListCrudRepository<Ticket, Long>, ListPagingAndSortingRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE " +
            "t.customizedCategory.category.id = ?1 ")
    List<Ticket> getTicketByCategoryId(Long categoryID);
}
