package com.corusoft.ticketmanager.tickets.services.filters;

import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.TicketFilterParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class TicketFilter {
    private StoreCriteria storeCriteria;
    private EmisionDateCriteria emisionDateRangeCriteria;
    private AmountCriteria amountRangeCriteria;

    public static TicketFilterBuilder builder() {
        return new TicketFilterBuilder();
    }


    public List<Ticket> filter(List<Ticket> tickets) {
        // La salida de un filtro es la entrada de otro
        // Huele mucho a cadena de responabilidad, pero me da pereza implementarla ahora ;)
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
        private EmisionDateCriteria emisionDateRangeCriteria;
        private AmountCriteria amountRangeCriteria;


        /* ******************** Métodos builder ******************** */
        public TicketFilterBuilder withStoreCriteria(String store) {
            this.storeCriteria = new StoreCriteria(store);
            return this;
        }

        public TicketFilterBuilder withEmisionDateRangeCriteria(LocalDateTime initialDate, LocalDateTime finalDate) {
            this.emisionDateRangeCriteria = new EmisionDateCriteria(initialDate, finalDate);
            return this;
        }

        public TicketFilterBuilder withAmountRangeCriteria(Float lowerBound, Float upperBound) {
            this.amountRangeCriteria = new AmountCriteria(lowerBound, upperBound);
            return this;
        }

        public TicketFilter buildFromDTO(TicketFilterParamsDTO dto) {
            return TicketFilter.builder()
                    .withStoreCriteria(dto.getStoreCriteria().getStore())
                    .withEmisionDateRangeCriteria(
                            dto.getEmisionDateCriteria().getInitialDate(),
                            dto.getEmisionDateCriteria().getInitialDate())
                    .withAmountRangeCriteria(dto.getAmountCriteria().getLowerBound(), dto.getAmountCriteria().getUpperBound())
                    .build();
        }

        public TicketFilter build() {
            return new TicketFilter(storeCriteria, emisionDateRangeCriteria, amountRangeCriteria);
        }
    }
}
