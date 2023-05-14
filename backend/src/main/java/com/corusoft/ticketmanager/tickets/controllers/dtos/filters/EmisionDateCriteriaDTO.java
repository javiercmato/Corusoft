package com.corusoft.ticketmanager.tickets.controllers.dtos.filters;

import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmisionDateCriteriaDTO {
    @PastOrPresent
    private LocalDateTime initialDate;

    @PastOrPresent
    private LocalDateTime finalDate;
}
