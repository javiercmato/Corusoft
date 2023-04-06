package com.corusoft.ticketmanager.tickets.controllers.dtos.conversors;

import com.corusoft.ticketmanager.tickets.controllers.dtos.ParsedTicketDTO;
import com.corusoft.ticketmanager.tickets.entities.ParsedTicketData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketConversor {
    /* ******************** Convertir a DTO ******************** */
    public static ParsedTicketDTO toParsedTicketDTO(ParsedTicketData entity) {
        return ParsedTicketDTO.builder()
                .id(entity.getId())
                .registeredAt(entity.getRegistered_at())
                .supplier(entity.getSupplier())
                .category(entity.getCategory())
                .subcategory(entity.getSubcategory())
                .emmitedAtDate(entity.getEmitted_at_date())
                .emmitedAtTime(entity.getEmitted_at_time())
                .country(entity.getCountry())
                .currency(entity.getCurrency())
                .totalTax(entity.getTotal_tax())
                .totalAmount(entity.getTotal_amount())
                .build();
    }

    /* ******************** Convertir a conjunto de DTOs ******************** */


    /* ******************** Convertir a Entidad ******************** */

}
