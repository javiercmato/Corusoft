package com.corusoft.ticketmanager.backend.dtos.tickets

import java.time.LocalDate
import java.time.LocalDateTime

data class ParsedTicketDTO(
    val id: Long?,
    val registeredAt: LocalDateTime?,
    val supplier: String?,
    val category: String?,
    val subcategory: String?,
    val emmitedAtDate: LocalDate?,
    val emmitedAtTime: String?,
    val country: String?,
    val currency: String?,
    val totalTax: Float?,
    val totalAmount: Float?
)
