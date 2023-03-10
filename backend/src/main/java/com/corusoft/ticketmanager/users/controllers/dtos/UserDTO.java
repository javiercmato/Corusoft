package com.corusoft.ticketmanager.users.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long userID;
    private String role;
    @NotBlank(groups = {AllValidations.class})
    @Size(min = 1, max = 30, groups = {AllValidations.class})
    private String nickname;
    @NotBlank(groups = {AllValidations.class})
    @Size(min = 1, max = 30, groups = {AllValidations.class, UpdateValidations.class})
    private String name;
    @NotBlank(groups = {AllValidations.class})
    @Size(min = 1, max = 100, groups = {AllValidations.class, UpdateValidations.class})
    @Email(groups = {AllValidations.class, UpdateValidations.class, UpdateValidations.class})
    private String email;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @PastOrPresent(groups = {UpdateValidations.class})
    private LocalDateTime registeredAt;
    @NotBlank(groups = {AllValidations.class})
    @JsonProperty(value = "isSubscribed")
    private boolean isSubscribed;

    /**
     * Atributos que debe contener obligatoriamente
     */
    public interface AllValidations {
    }

    /**
     * Atributos que debe contener al actualizar datos del usuario
     */
    public interface UpdateValidations {
    }
}
