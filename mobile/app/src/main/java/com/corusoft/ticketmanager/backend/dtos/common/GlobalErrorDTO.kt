package com.corusoft.ticketmanager.backend.dtos.common

import com.fasterxml.jackson.annotation.JsonProperty

data class GlobalErrorDTO(
    @JsonProperty("globalError")
    val globalError: String?
) {
    constructor() : this(null)
}
