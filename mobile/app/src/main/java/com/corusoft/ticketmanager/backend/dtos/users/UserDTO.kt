package com.corusoft.ticketmanager.backend.dtos.users

import java.time.LocalDateTime

data class UserDTO (
    var userID: Long? = null,
    var role: String? = null,
    var nickname: String? = null,
    var name: String? = null,
    var email: String? = null,
    var registeredAt: LocalDateTime? = null,
    var isSubscribed: Boolean? = null
)
