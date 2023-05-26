package com.corusoft.ticketmanager.tickets.services.filters;

import com.corusoft.ticketmanager.common.filters.FilterCriteria;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class StoreCriteria implements FilterCriteria {
    private String storeName;

    @Override
    public List<Ticket> filterByCriteria(List<Ticket> tickets) {
        List<Ticket> results = new ArrayList<>();

        for (Ticket ticket: tickets) {
            if (ticket.getStore().equalsIgnoreCase(storeName)) {
                results.add(ticket);
            }
        }

        return results;
    }
}
