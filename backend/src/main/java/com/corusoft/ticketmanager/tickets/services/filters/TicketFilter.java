package com.corusoft.ticketmanager.tickets.services.filters;

import com.corusoft.ticketmanager.common.filters.FilterCriteria;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.TicketFilterParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
public class TicketFilter {
    private final StoreCriteria storeCriteria;
    private final EmisionDateCriteria emisionDateRangeCriteria;
    private final AmountCriteria amountRangeCriteria;
    private final List<FilterCriteria> criteria = new ArrayList<>();


    private TicketFilter(StoreCriteria storeCriteria, EmisionDateCriteria emisionDateRangeCriteria, AmountCriteria amountRangeCriteria) {
        this.storeCriteria = storeCriteria;
        criteria.add(storeCriteria);
        this.emisionDateRangeCriteria = emisionDateRangeCriteria;
        criteria.add(emisionDateRangeCriteria);
        this.amountRangeCriteria = amountRangeCriteria;
        criteria.add(amountRangeCriteria);
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

    public static TicketFilterBuilder builder() {
        return new TicketFilterBuilder();
    }

    public boolean hasAnyCriteria() {
        return this.criteria.isEmpty();
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
            TicketFilterBuilder builder = TicketFilter.builder();

            if (dto.getStoreCriteria() != null) {
                builder = builder.withStoreCriteria(dto.getStoreCriteria().getStore());
            }
            if (dto.getStoreCriteria() != null) {
                builder = builder.withEmisionDateRangeCriteria(
                            dto.getEmisionDateCriteria().getInitialDate(),
                            dto.getEmisionDateCriteria().getInitialDate()
                );
            }
            if (dto.getStoreCriteria() != null) {
                builder = builder.withAmountRangeCriteria(
                        dto.getAmountCriteria().getLowerBound(),
                        dto.getAmountCriteria().getUpperBound()
                );
            }

            return builder.build();
        }

        public TicketFilter build() {
            return new TicketFilter(storeCriteria, emisionDateRangeCriteria, amountRangeCriteria);
        }
    }
}
