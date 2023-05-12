package com.corusoft.ticketmanager.backend.dtos.common

import com.fasterxml.jackson.annotation.JsonProperty

data class GlobalError(
    @JsonProperty("globalError")
    val globalError: String?
) {
    constructor() : this(null)
}


