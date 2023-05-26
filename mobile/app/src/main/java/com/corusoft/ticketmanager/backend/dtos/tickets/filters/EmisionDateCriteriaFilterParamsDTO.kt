package com.corusoft.ticketmanager.backend.dtos.tickets.filters

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

data class EmisionDateCriteriaFilterParamsDTO(
    val initialDate: String?,
    val finalDate: String?
) {
    constructor() : this(null, null)
    constructor(initialDate: LocalDate, finalDate: LocalDate) : this(
        initialDate.let { initialDate.format(formatter) } ?: null,
        finalDate.let { finalDate.format(formatter) } ?: null
    )
}
