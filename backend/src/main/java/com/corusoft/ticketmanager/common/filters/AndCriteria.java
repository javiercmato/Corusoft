package com.corusoft.ticketmanager.common.filters;

import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AndCriteria implements FilterCriteria {
    private FilterCriteria firstCriteria;
    private FilterCriteria otherCriteria;

    @Override
    public List<Ticket> filterByCriteria(List<Ticket> tickets) {
        List<Ticket> firstCriteriaResults = firstCriteria.filterByCriteria(tickets);

        return otherCriteria.filterByCriteria(firstCriteriaResults);
    }
}
