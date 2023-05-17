package com.corusoft.ticketmanager.backend.dtos.tickets.filters

data class AmountCriteriaFilterParamsDTO(
    val lowerBound: Float?,
    val upperBound: Float?
) {
    constructor(): this(null, null)
}
