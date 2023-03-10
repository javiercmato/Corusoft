package com.corusoft.ticketmanager.users.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUserDTO {
    @JsonProperty(value = "serviceToken")
    private String serviceToken;

    @JsonProperty(value = "user")
    private UserDTO userDTO;
}
