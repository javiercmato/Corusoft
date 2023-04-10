package com.corusoft.ticketmanager.users.entities;

import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<CustomizedCategory> customizedCategories = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
        name = "TicketReceived",
        joinColumns = @JoinColumn(name = "receiver_id"),
        inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private Set<Ticket> sharedTickets = new LinkedHashSet<>();

    /* *************** Métodos de entidad *************** */

    /**
     * Comprobar si el usuario tiene alguna subscripción activa
     */
    @Transient
    public Boolean hasSubscriptionActive() {
        return subscriptions.stream()
                .anyMatch((sub) -> sub.getStatus().equals(SubscriptionStatus.ACTIVE));
    }

    /**
     * Asignar una subscripción al usuario
     * @param subscription Subscripción a añadir al usuario
     */
    @Transient
    public void assignSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setUser(this);
    }

    /**
     * Asigna una categoría personalizada al usuario
     * @param customCategory Categoría personalizada a asignar
     */
    @Transient
    public void assignCustomizedCategory(CustomizedCategory customCategory) {
        customizedCategories.add(customCategory);
        customCategory.setUser(this);
    }

    /**
     * Asigna un ticket al usuario actual.
     * @param ticket - Ticket a asignar
     */
    @Transient
    public void assignTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setCreator(this);
    }

    /**
     * Comparte un Ticket a este usuario.
     * @param ticket - Ticket a compartir
     */
    @Transient
    public void shareTicket(Ticket ticket) {
        sharedTickets.add(ticket);
        ticket.sharedUsers(this);
    }

}
