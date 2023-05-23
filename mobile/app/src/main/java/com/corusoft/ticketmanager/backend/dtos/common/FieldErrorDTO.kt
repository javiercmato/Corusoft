package com.corusoft.ticketmanager.backend.dtos.common

data class FieldErrorDTO(
    val fieldName: String?,
    val message: String?
) {
    constructor() : this(null, null)

    override fun toString(): String {
        return "Error en $fieldName: $message"
    }
}
