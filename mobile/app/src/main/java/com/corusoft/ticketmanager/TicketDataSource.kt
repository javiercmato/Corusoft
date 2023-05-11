package com.corusoft.ticketmanager

import com.corusoft.ticketmanager.backend.dtos.tickets.CustomizedCategoryID
import com.corusoft.ticketmanager.backend.dtos.tickets.TicketDTO
import java.time.LocalDateTime

// Delete this class when Http is implemented for Get Tickets
class TicketDataSource {

    fun createTicket(x: Int): TicketDTO {
        return TicketDTO(x.toLong(),
            x.toString(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            x.toFloat(),
            x.toString(),
            x.toString(),
            x.toString(),
            CustomizedCategoryID(1, 1),
            x.toLong()
        )
    }

    fun loadTickets(): List<TicketDTO> {
        return listOf<TicketDTO>(
            createTicket(1),
            createTicket(2),
            createTicket(3),
            createTicket(4),
            createTicket(5),
            createTicket(6),
            createTicket(7),
            createTicket(8),
            createTicket(9),
            createTicket(10)
        )
    }
}