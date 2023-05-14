package com.corusoft.ticketmanager.tickets.controllers.dtos.filters;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketFilterParamsDTO {
    private StoreCriteriaDTO storeCriteria;

    private EmisionDateCriteriaDTO emisionDateCriteria;

    private AmountCriteriaDTO amountCriteria;
}
