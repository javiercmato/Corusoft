package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.ParsedTicketData;
import org.springframework.data.repository.ListCrudRepository;

public interface ParsedTicketDataRepository extends ListCrudRepository<ParsedTicketData, Long> {

}
