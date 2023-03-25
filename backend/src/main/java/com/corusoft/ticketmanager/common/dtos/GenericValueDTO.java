package com.corusoft.ticketmanager.common.dtos;

import lombok.*;

/**
 * Clase para transmitir un valor de tipo <c>T</c>.
 * @param <T> Tipo de dato del valor a transmitir
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericValueDTO<T> {
    private T value;
}
