package com.corusoft.ticketmanager.users.entities;

import com.corusoft.ticketmanager.tickets.entities.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usertable")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "nickname", nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registered_at;


    /* *************** Asociaciones con otras entidades *************** */
    @OneToMany(mappedBy = "creator", orphanRemoval = true)
    private Set<Ticket> tickets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Subscription> subscriptions = new LinkedHashSet<>();


    /* *************** Métodos de entidad *************** */

    /**
     * Comprobar si el usuario tiene alguna subscripción activa
     */
    @Transient
    public Boolean hasSubscriptionActive() {
        return subscriptions.stream()
                .anyMatch((sub) -> sub.getStatus().equals(SubscriptionStatus.ACTIVE));
    }

    @Transient
    public void assignSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setUser(this);
    }

}
