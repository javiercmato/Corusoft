package com.corusoft.ticketmanager.backend.dtos.tickets

data class ShareTicketParamsDTO(
    val senderID: Long?,
    val receiverName: String?
)
