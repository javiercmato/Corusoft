package com.corusoft.ticketmanager.tickets.controllers.dtos.filters;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmisionDateCriteriaDTO {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime initialDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime finalDate;
}
