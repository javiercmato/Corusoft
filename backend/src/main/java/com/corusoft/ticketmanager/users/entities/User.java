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
        name = "SharedTicket",
        joinColumns = @JoinColumn(name = "receiver_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "ticket_id", nullable = false))
    private Set<Ticket> sharedTickets = new LinkedHashSet<>();

    /* *************** Métodos de entidad *************** */
    /**
     * Comprobar si el usuario tiene alguna subscripción activa
     */
    @Transient
    public Boolean hasSubscriptionActive() {
        if (this.subscriptions.isEmpty()) {
            return false;
        }

        return subscriptions.stream()
                .anyMatch((sub) -> sub.getStatus().equals(SubscriptionStatus.ACTIVE));
    }

    /**
     * Asignar una subscripción al usuario
     * @param subscription Subscripción a añadir al usuario
     */
    @Transient
    public void assignSubscription(Subscription subscription) {
        if (this.subscriptions == null) {
            this.subscriptions = new LinkedHashSet<>();
        }

        subscriptions.add(subscription);
        subscription.setUser(this);
    }

    /**
     * Asigna una categoría personalizada al usuario
     * @param customCategory Categoría personalizada a asignar
     */
    @Transient
    public void assignCustomizedCategory(CustomizedCategory customCategory) {
        if (this.customizedCategories == null) {
            this.customizedCategories = new LinkedHashSet<>();
        }

        customizedCategories.add(customCategory);
        customCategory.setUser(this);
    }

    /**
     * Asigna un ticket al usuario actual.
     *
     * @param ticket Ticket a asignar
     */
    @Transient
    public void assignTicket(Ticket ticket) {
        if (this.tickets == null) {
            this.tickets = new LinkedHashSet<>();
        }

        tickets.add(ticket);
        ticket.setCreator(this);
    }

    /**
     * Indica si el usuario ha compartido el ticket recibido con otro usuario
     *
     * @param ticket Ticket a comprobar
     * @return <c>true</c> si usuario ha compartido el ticket
     */
    @Transient
    public boolean hasSharedTicket(Ticket ticket) {
        if (this.sharedTickets == null)
            return false;

        return this.sharedTickets.contains(ticket);
    }

}
