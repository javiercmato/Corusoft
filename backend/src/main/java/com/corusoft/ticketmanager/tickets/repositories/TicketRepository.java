package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.Ticket;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface TicketRepository extends ListCrudRepository<Ticket, Long>, ListPagingAndSortingRepository<Ticket, Long> {

}
