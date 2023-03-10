package com.corusoft.ticketmanager.tickets.entities;

import com.corusoft.ticketmanager.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "emitted_at", nullable = false)
    private LocalDateTime emittedAt;

    @Column(name = "amount", nullable = false, precision = 2)
    private Float amount;

    @Lob
    @Column(name = "picture", nullable = false)
    private byte[] picture;


    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "custom_category_user_id", referencedColumnName = "USER_ID", nullable = false),
            @JoinColumn(name = "custom_category_category_id", referencedColumnName = "CATEGORY_ID", nullable = false)
    })
    private CustomizedCategory customizedCategory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "parsed_ticket_id", nullable = false, unique = true)
    private ParsedTicketData parsedTicketData;

}
