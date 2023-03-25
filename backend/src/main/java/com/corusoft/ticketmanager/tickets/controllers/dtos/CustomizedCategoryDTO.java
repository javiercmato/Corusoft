package com.corusoft.ticketmanager.tickets.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomizedCategoryDTO {

    private String categoryName;

    @NotNull
    @Positive
    private Float maxWasteLimit;
}
