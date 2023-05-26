package com.corusoft.ticketmanager.backend.dtos.tickets

data class TicketDTO(
    val id: Long,
    val name: String?,
    val registeredAt: String?,
    val emmitedAt: String?,
    val amount: Float?,
    val currency: String?,
    val picture: String?,
    val store: String?,
    val customizedCategoryID: CustomizedCategoryID?,
    val creatorID: Long?,
    var expandable: Boolean = false
)
