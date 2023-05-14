package com.corusoft.ticketmanager.tickets.services.filters;

import com.corusoft.ticketmanager.common.filters.AndCriteria;
import com.corusoft.ticketmanager.common.filters.FilterCriteria;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EmisionDateRangeCriteria implements FilterCriteria {
    private LocalDateTime initialDate;
    private LocalDateTime finalDate;

    @Override
    public List<Ticket> filterByCriteria(List<Ticket> tickets) {
        boolean hasInitialDate = initialDate != null;
        boolean hasFinalDate = finalDate != null;
        FilterCriteria criteria;

        // Caso 1: hay fecha inicial y final
        if (hasInitialDate && hasFinalDate) {
            criteria = new AndCriteria(new InitialDateCriteria(initialDate), new FinalDateCriteria(finalDate));
            return criteria.filterByCriteria(tickets);
        }

        // Caso 2: hay fecha inicial pero no fecha final
        if (hasInitialDate && !hasFinalDate) {
            criteria = new InitialDateCriteria(initialDate);
            return criteria.filterByCriteria(tickets);
        }

        // Caso 3: no hay fecha inicial pero sí fecha final
        if (!hasInitialDate && hasFinalDate) {
            criteria = new FinalDateCriteria(finalDate);
            return criteria.filterByCriteria(tickets);
        }

        // Caso 4: no hay rango de fechas: devolver lista recibida
        return tickets;
    }

    private static class InitialDateCriteria implements FilterCriteria {
        private final LocalDateTime initialDate;

        public InitialDateCriteria(LocalDateTime initialDate) {
            this.initialDate = initialDate
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
        }

        @Override
        public List<Ticket> filterByCriteria(List<Ticket> tickets) {
            Predicate<Ticket> isAfterReceivedDate = ticket ->
                    ticket.getEmittedAt().isEqual(initialDate)
                    || ticket.getEmittedAt().isAfter(initialDate);
            return tickets.stream()
                    .filter(isAfterReceivedDate)
                    .collect(Collectors.toList());
        }
    }

    private static class FinalDateCriteria implements FilterCriteria {
        private final LocalDateTime finalDate;

        public FinalDateCriteria(LocalDateTime initialDate) {
            this.finalDate = initialDate
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59)
                    .withNano(999999999);
        }

        @Override
        public List<Ticket> filterByCriteria(List<Ticket> tickets) {
            Predicate<Ticket> isBeforeReceivedDate = ticket ->
                    ticket.getEmittedAt().isEqual(finalDate)
                            || ticket.getEmittedAt().isBefore(finalDate);
            return tickets.stream()
                    .filter(isBeforeReceivedDate)
                    .collect(Collectors.toList());
        }
    }
}
