package com.corusoft.ticketmanager.backend.dtos.tickets

data class CustomizedCategoryDTO(
    val id: CustomizedCategoryID,
    val name: String?,
    val maxWasteLimit: Float?
)
