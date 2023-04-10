package com.corusoft.ticketmanager.tickets.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShareParamsDTO {

    @NotBlank
    private String receiverName;
}
