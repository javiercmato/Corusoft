package com.corusoft.ticketmanager.backend.dtos.common

data class BlockDTO<T>(
    var items: List<T>,
    var hasMoreItems: Boolean,
    var itemsCount: Int
)
