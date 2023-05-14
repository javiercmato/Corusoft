package com.corusoft.ticketmanager.common.filters;

import com.corusoft.ticketmanager.tickets.entities.Ticket;

import java.util.List;

public interface FilterCriteria {
    List<Ticket> filterByCriteria(List<Ticket> tickets);
}
