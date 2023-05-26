package com.corusoft.ticketmanager.tickets.controllers.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedTicketDTO {
    private Long id;

    @PastOrPresent
    private LocalDateTime registeredAt;

    @NotBlank
    private String supplier;

    @NotBlank
    private String category;

    @NotBlank
    private String subcategory;

    @PastOrPresent
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
}
