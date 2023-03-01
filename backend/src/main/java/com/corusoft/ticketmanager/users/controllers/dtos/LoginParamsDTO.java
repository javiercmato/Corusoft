package com.corusoft.ticketmanager.users.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParamsDTO {
    @NotBlank
    @Size(min = 1, max = 30)
    private String nickname;

    @NotBlank
    @Size(min = 1)
    private String password;
}
