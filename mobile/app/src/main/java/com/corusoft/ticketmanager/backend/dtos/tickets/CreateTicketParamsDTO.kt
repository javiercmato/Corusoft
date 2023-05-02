package com.corusoft.ticketmanager.backend.dtos.tickets

import java.time.LocalDate


data class CreateTicketParamsDTO(
    val userID: Long,
    val supplier: String,
    val categoryID: Long,
    val emmitedAtDate: LocalDate?,
    val emmitedAtTime: String,
    val country: String,
    val currency: String,
    val totalTax: Float,
    val totalAmount: Float,
    val ticketData: String,
    val name: String
)
