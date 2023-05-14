package com.corusoft.ticketmanager.tickets.controllers.dtos.filters;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountCriteriaDTO {
    @PositiveOrZero
    private Float lowerBound;

    @PositiveOrZero
    private Float upperBound;
}
