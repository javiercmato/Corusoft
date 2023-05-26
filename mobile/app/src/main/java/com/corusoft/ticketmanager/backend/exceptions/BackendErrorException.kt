package com.corusoft.ticketmanager.backend.exceptions

import com.corusoft.ticketmanager.backend.dtos.common.ErrorsDTO
import com.corusoft.ticketmanager.backend.dtos.common.FieldErrorDTO

class BackendErrorException(
    val globalError: String?,
    val fieldErrors: List<FieldErrorDTO>?
) : Exception() {
    constructor(errors: ErrorsDTO) : this(errors.globalError, errors.fieldErrors)

    fun getDetails(): String {
        if (globalError != null) return globalError

        if (fieldErrors != null) {
            return if (fieldErrors.size == 1) {
                fieldErrors.first().toString()
            } else {
                "Hay más de un campo con valores inválidos"
            }
        }

        return "Hay campos con valores incorrectos"
    }
}
