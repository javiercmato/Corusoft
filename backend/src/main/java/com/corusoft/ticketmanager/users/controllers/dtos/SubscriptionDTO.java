package com.corusoft.ticketmanager.users.controllers.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SubscriptionDTO {
    @NotNull
    private Long subscriptionID;

    @NotBlank
    private String status;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @PastOrPresent
    private LocalDateTime registeredAt;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @PastOrPresent
    private LocalDateTime endingAt;
}
