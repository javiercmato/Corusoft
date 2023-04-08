package com.corusoft.ticketmanager.tickets.controllers.dtos.conversors;

import com.corusoft.ticketmanager.tickets.controllers.dtos.ParsedTicketDTO;
import com.corusoft.ticketmanager.tickets.controllers.dtos.TicketDTO;
import com.corusoft.ticketmanager.tickets.entities.ParsedTicketData;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

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

    public static TicketDTO toTicketDTO(Ticket entity) {
        String pictureAsB64String = Base64.getEncoder().encodeToString(entity.getPicture());

        return TicketDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .registeredAt(entity.getRegisteredAt())
                .emmitedAt(entity.getEmittedAt())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .picture(pictureAsB64String)
                .store(entity.getStore())
                .customizedCategoryID(entity.getCustomizedCategory().getId())
                .creatorID(entity.getCreator().getId())
                .build();
    }
    /* ******************** Convertir a conjunto de DTOs ******************** */


    /* ******************** Convertir a Entidad ******************** */

}
