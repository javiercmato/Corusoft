package com.corusoft.ticketmanager.backend.dtos.tickets

import java.time.LocalDateTime

data class TicketDTO(
    val id: Long,
    val name: String?,
    val registeredAt: LocalDateTime?,
    val emmitedAt: LocalDateTime?,
    val amount: Float?,
    val currency: String?,
    val picture: String?,
    val store: String?,
    val customizedCategoryID: CustomizedCategoryID?,
    val creatorID: Long?
)