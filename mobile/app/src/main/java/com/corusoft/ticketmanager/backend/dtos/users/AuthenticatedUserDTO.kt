package com.corusoft.ticketmanager.backend.dtos.users

data class AuthenticatedUserDTO(
    var serviceToken: String,
    var userDTO: UserDTO
)
