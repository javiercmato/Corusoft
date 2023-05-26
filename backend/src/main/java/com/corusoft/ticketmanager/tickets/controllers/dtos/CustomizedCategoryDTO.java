package com.corusoft.ticketmanager.tickets.controllers.dtos;

import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomizedCategoryDTO {
    @NotNull
    private CustomizedCategoryID id;

    private String name;

    @NotNull
    @Positive
    private Float maxWasteLimit;
}
