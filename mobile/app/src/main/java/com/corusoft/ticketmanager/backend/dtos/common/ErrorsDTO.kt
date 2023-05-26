package com.corusoft.ticketmanager.backend.dtos.common

data class ErrorsDTO(
    val globalError: String? = null,
    val fieldErrors: List<FieldErrorDTO>? = null
)
