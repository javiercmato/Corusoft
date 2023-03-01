package com.corusoft.ticketmanager.users.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Column(name = "starting_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "ending_at", nullable = false)
    private LocalDateTime endingAt;


    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User user;

}
