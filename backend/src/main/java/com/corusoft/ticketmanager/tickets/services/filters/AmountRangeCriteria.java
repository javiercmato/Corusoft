package com.corusoft.ticketmanager.tickets.services.filters;

import com.corusoft.ticketmanager.common.filters.AndCriteria;
import com.corusoft.ticketmanager.common.filters.FilterCriteria;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AmountRangeCriteria implements FilterCriteria {
    private Float lowerBound;
    private Float upperBound;

    @Override
    public List<Ticket> filterByCriteria(List<Ticket> tickets) {
        boolean hasLowerBound = lowerBound != null;
        boolean hasUpperBound = upperBound != null;
        FilterCriteria criteria;

        // Caso 1: hay cotas inferior y superior
        if (hasLowerBound && hasUpperBound) {
            criteria = new AndCriteria(new LowerBoundCriteria(lowerBound), new UpperBoundCriteria(upperBound));
            return criteria.filterByCriteria(tickets);
        }

        // Caso 2: hay cota inferior pero no cota superior
        if (hasLowerBound && !hasUpperBound) {
            criteria = new LowerBoundCriteria(lowerBound);
            return criteria.filterByCriteria(tickets);
        }

        // Caso 3: no hay cota inferior pero sí cota superior
        if (!hasLowerBound && hasUpperBound) {
            criteria = new UpperBoundCriteria(upperBound);
            return criteria.filterByCriteria(tickets);
        }

        // Caso 4: no hay cotas: devolver lista recibida
        return tickets;
    }

    @AllArgsConstructor
    private static class LowerBoundCriteria implements FilterCriteria {
        private Float lowerBound;

        @Override
        public List<Ticket> filterByCriteria(List<Ticket> tickets) {
            Predicate<Ticket> isBigger = ticket -> ticket.getAmount() >= lowerBound;
            return tickets.stream()
                    .filter(isBigger)
                    .collect(Collectors.toList());
        }
    }

    @AllArgsConstructor
    private static class UpperBoundCriteria implements FilterCriteria {
        private Float upperBound;

        @Override
        public List<Ticket> filterByCriteria(List<Ticket> tickets) {
            Predicate<Ticket> isLower = ticket -> ticket.getAmount() <= upperBound;
            return tickets.stream()
                    .filter(isLower)
                    .collect(Collectors.toList());
        }
    }
}
