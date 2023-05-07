package com.corusoft.ticketmanager.backend.dtos.users

import java.time.LocalDateTime

data class SubscriptionDTO(
    var subscriptionID: Long?,
    var status: String,
    var registeredAt: LocalDateTime?,
    var endingAt: LocalDateTime?
)
