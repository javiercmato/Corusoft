package com.corusoft.ticketmanager.tickets.controllers.dtos;

import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Long id;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDateTime registeredAt;

    @NotNull
    @PastOrPresent
    private LocalDateTime emmitedAt;

    @NotNull
    private Float amount;

    @NotNull
    private String currency;

    @NotNull
    private String picture;

    private String store;

    private CustomizedCategoryID customizedCategoryID;

    @NotNull
    private Long creatorID;
}
