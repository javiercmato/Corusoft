package com.corusoft.ticketmanager.tickets.entities;

import com.corusoft.ticketmanager.users.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customizedcategory")
public class CustomizedCategory {
    @EmbeddedId
    private CustomizedCategoryID id = new CustomizedCategoryID();

    @Column(name = "name", nullable = false, length = 50)
    private String name;

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
