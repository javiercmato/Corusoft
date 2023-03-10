package com.corusoft.ticketmanager.users.controllers.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterUserParamsDTO {
    @NotBlank
    @Size(min = 1, max = 30)
    private String nickname;

    @NotBlank
    @Size(min = 1)
    private String rawPassword;

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank
    @Email
    @Size(min = 1, max = 100)
    private String email;
}
