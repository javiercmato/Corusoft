package com.corusoft.ticketmanager.tickets.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parsedticketdata")
public class ParsedTicketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "parsed_at", nullable = false)
    private LocalDateTime registered_at;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "category")
    private String category;

    @Column(name = "subcategory")
    private String subcategory;

    @Column(name = "emitted_at_date")
    private LocalDate emitted_at_date;

    @Column(name = "emitted_at_time")
    private String emitted_at_time;

    @Column(name = "country")
    private String country;

    @Column(name = "language")
    private String language;

    @Column(name = "currency")
    private String currency;

    @Column(name = "total_tax")
    private Float total_tax;

    @Column(name = "total_amount")
    private Float total_amount;
}
