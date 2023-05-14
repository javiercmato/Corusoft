package com.corusoft.ticketmanager.tickets.controllers.dtos.filters;

import lombok.*;

@Builder
@Getter
@ToString
public class TicketFilterParamsDTO {
    private StoreCriteriaDTO storeCriteria;

    private EmisionDateCriteriaDTO emisionDateCriteria;

    private AmountCriteriaDTO amountCriteria;
}
