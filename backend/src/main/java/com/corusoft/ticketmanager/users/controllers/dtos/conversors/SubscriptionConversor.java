package com.corusoft.ticketmanager.users.controllers.dtos.conversors;

import com.corusoft.ticketmanager.users.controllers.dtos.SubscriptionDTO;
import com.corusoft.ticketmanager.users.entities.Subscription;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionConversor {
    /* ******************** Convertir a DTO ******************** */
    public static SubscriptionDTO toSubscriptionDTO(Subscription entity) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setSubscriptionID(entity.getId());
        dto.setStatus(entity.getStatus().toString());
        dto.setRegisteredAt(entity.getRegisteredAt());
        dto.setEndingAt(entity.getEndingAt());

        return dto;
    }

    /* ******************** Convertir a conjunto de DTOs ******************** */

}
