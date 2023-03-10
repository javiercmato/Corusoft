package com.corusoft.ticketmanager.common.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class Block<T> {
    /**
     * Elementos almacenados
     */
    private List<T> items;

    /**
     * Indica si hay más elementos
     */
    @Getter(AccessLevel.NONE)
    private boolean hasMoreItems;

    /**
     * Cantidad de elementos contenidos
     */
    private int itemsCount;

    public boolean hasMoreItems() {
        return this.hasMoreItems;
    }
}
