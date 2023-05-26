package com.corusoft.ticketmanager.tickets.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WastesPerCategoryParams {

    @NotNull
    private Long categoryId;
}
