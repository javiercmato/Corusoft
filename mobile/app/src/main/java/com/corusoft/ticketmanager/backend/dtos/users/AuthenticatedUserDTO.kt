package com.corusoft.ticketmanager.backend.dtos.users

data class AuthenticatedUserDTO(
    var serviceToken: String?,
    var user: UserDTO?
) {
    constructor() : this(null, null)
}
