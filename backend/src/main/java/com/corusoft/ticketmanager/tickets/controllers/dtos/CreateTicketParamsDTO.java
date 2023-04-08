package com.corusoft.ticketmanager.tickets.controllers.dtos;

import com.corusoft.ticketmanager.common.JacksonLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTicketParamsDTO {
    @NotNull
    private Long userID;

    @NotBlank
    private String supplier;

    @NotNull
    private Long categoryID;

    @PastOrPresent
    @JsonDeserialize(using = JacksonLocalDateDeserializer.class)
    private LocalDate emmitedAtDate;

    @NotBlank
    private String emmitedAtTime;

    @NotBlank
    private String country;

    @NotBlank
    private String currency;

    @PositiveOrZero
    private Float totalTax;

    @PositiveOrZero
    private Float totalAmount;

    /** Ticket codificado como String en Base64 */
    @NotBlank
    private String ticketData;

    @NotBlank
    private String name;
}
