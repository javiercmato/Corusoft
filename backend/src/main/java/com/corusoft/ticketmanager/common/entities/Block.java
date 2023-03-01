package com.corusoft.ticketmanager.common.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

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
