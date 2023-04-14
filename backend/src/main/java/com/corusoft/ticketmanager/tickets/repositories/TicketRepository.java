package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.controllers.dtos.SpendingPerMonthsDTO;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import com.corusoft.ticketmanager.tickets.services.utils.Spendings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface TicketRepository extends ListCrudRepository<Ticket, Long>, ListPagingAndSortingRepository<Ticket, Long> {

    @Query("Select new com.corusoft.ticketmanager.tickets.services.utils.Spendings(t.registeredAt, t.amount) " +
            "From Ticket t " +
            "Where t.creator.id = ?1 " +
            "order by t.registeredAt DESC ")
    List<Spendings> findUserSpendings(Long userId);
}
