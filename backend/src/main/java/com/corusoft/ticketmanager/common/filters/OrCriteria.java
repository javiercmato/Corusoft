package com.corusoft.ticketmanager.common.filters;

import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OrCriteria implements FilterCriteria {
    private FilterCriteria firstCriteria;
    private FilterCriteria otherCriteria;

    @Override
    public List<Ticket> filterByCriteria(List<Ticket> tickets) {
        List<Ticket> firstCriteriaResults = firstCriteria.filterByCriteria(tickets);
        List<Ticket> otherCriteriaResults = otherCriteria.filterByCriteria(tickets);

        for (Ticket ticket: otherCriteriaResults) {
            if (!firstCriteriaResults.contains(ticket)) {
                firstCriteriaResults.add(ticket);
            }
        }

        return firstCriteriaResults;
    }
}
