package com.corusoft.ticketmanager.backend.dtos.users

data class RegisterUserParamsDTO(
    var nickname: String,
    var rawPassword: String,
    var name: String,
    var email: String
)
