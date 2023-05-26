package com.corusoft.ticketmanager.tickets.entities;

import com.corusoft.ticketmanager.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customizedcategory")
public class CustomizedCategory {

    @EmbeddedId
    private CustomizedCategoryID id = new CustomizedCategoryID();

    @Column(name = "max_waste_limit", nullable = false, precision = 12)
    private Float maxWasteLimit;


    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne
    @MapsId("userID")
    private User user;

    @ManyToOne
    @MapsId("categoryID")
    private Category category;
}
