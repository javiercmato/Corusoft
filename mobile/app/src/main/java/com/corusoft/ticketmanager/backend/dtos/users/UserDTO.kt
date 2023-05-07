package com.corusoft.ticketmanager.backend.dtos.users

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDTO (
    @JsonProperty("userID")
    var userID: Long? = null,
    @JsonProperty("role")
    var role: String? = null,
    @JsonProperty("nickname")
    var nickname: String? = null,
    @JsonProperty("name")
    var name: String? = null,
    @JsonProperty("email")
    var email: String? = null,
    @JsonProperty("registeredAt")
    var registeredAt: String? = null,
    @JsonProperty("isSubscribed")
    var isSubscribed: Boolean? = null
) {
    constructor() : this(null, null, null, null, null, null, null)
}

