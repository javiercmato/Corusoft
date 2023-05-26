package com.corusoft.ticketmanager.tickets.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareTicketParamsDTO {
    @NotNull
    private Long senderID;

    @NotNull
    private String receiverName;
}
