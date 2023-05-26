package com.corusoft.ticketmanager.tickets.controllers.dtos.filters;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreCriteriaDTO {
    private String store;

    public StoreCriteriaDTO(String store) {
        this.store = store.strip();
    }
}
