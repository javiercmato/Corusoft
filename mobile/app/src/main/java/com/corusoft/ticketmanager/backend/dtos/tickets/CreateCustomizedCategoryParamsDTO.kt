package com.corusoft.ticketmanager.backend.dtos.tickets

data class CreateCustomizedCategoryParamsDTO(
    var name: String = "",
    var maxWasteLimit: Float = 0.0f
)
