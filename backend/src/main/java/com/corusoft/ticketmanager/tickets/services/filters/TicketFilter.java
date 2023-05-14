package com.corusoft.ticketmanager.tickets.services.filters;

import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class TicketFilter {
    private StoreCriteria storeCriteria;
    private EmisionDateRangeCriteria emisionDateRangeCriteria;
    private AmountRangeCriteria amountRangeCriteria;

    public static TicketFilterBuilder builder() {
        return new TicketFilterBuilder();
    }

    public List<Ticket> filter(List<Ticket> tickets) {
        // La salida de un filtro es la entrada de otro
        // Huele mucho a cadena de responabilidad pero me da pereza implementarla ahora ;)
        List<Ticket> filteredTickets = tickets;
        if (this.storeCriteria != null) {
            filteredTickets = storeCriteria.filterByCriteria(tickets);
        }
        if (this.emisionDateRangeCriteria != null) {
            filteredTickets = emisionDateRangeCriteria.filterByCriteria(filteredTickets);
        }
        if (this.amountRangeCriteria != null) {
            filteredTickets = amountRangeCriteria.filterByCriteria(tickets);
        }

        return filteredTickets;
    }

    /** Builder de TicketFilter */
    @NoArgsConstructor
    public static class TicketFilterBuilder {
        private StoreCriteria storeCriteria;
        private EmisionDateRangeCriteria emisionDateRangeCriteria;
        private AmountRangeCriteria amountRangeCriteria;


        /* ******************** Métodos builder ******************** */
        public TicketFilterBuilder withStoreCriteria(String store) {
            this.storeCriteria = new StoreCriteria(store);
            return this;
        }

        public TicketFilterBuilder withEmisionDateRangeCriteria(LocalDateTime initialDate, LocalDateTime finalDate) {
            this.emisionDateRangeCriteria = new EmisionDateRangeCriteria(initialDate, finalDate);
            return this;
        }

        public TicketFilterBuilder WithAmountRangeCriteria(Float lowerBound, Float upperBound) {
            this.amountRangeCriteria = new AmountRangeCriteria(lowerBound, upperBound);
            return this;
        }

        public TicketFilter build() {
            return new TicketFilter(storeCriteria, emisionDateRangeCriteria, amountRangeCriteria);
        }
    }
}
