package com.corusoft.ticketmanager.tickets.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Clave primaria compuesta por ID de usuario e ID de categoría
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CustomizedCategoryID implements Serializable {
    @Column(table = "customizedcategory", name = "user_id", nullable = false)
    private Long userID;

    @Column(table = "customizedcategory", name = "category_id", nullable = false)
    private Long categoryID;

    @Override
    public String toString() {
        String sb = "UserID: " + userID + "; " +
                "CategoryID: " + categoryID + "; ";

        return sb;
    }
}
