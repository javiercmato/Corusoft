package com.corusoft.ticketmanager.backend.dtos.tickets.filters

import java.time.LocalDate

data class TicketFilterParamsDTO(
    var storeCriteria: StoreCriteriaFilterParamsDTO?,
    var emisionDateCriteria: EmisionDateCriteriaFilterParamsDTO?,
    var amountCriteria: AmountCriteriaFilterParamsDTO?
) {
    fun builder(): TicketFilterParamsDTOBuilder {
        return TicketFilterParamsDTOBuilder()
    }

    /** Builder de TicketFilterParamsDTO */
    class TicketFilterParamsDTOBuilder {
        var storeCriteria: StoreCriteriaFilterParamsDTO? = null
        var emisionDateCriteria: EmisionDateCriteriaFilterParamsDTO? = null
        var amountCriteria: AmountCriteriaFilterParamsDTO? = null

        fun withStore(store: String): TicketFilterParamsDTOBuilder {
            this.storeCriteria = StoreCriteriaFilterParamsDTO(store)
            return this
        }

        fun withEmisionDateRange(initialDate: LocalDate, finalDate: LocalDate): TicketFilterParamsDTOBuilder {
            this.emisionDateCriteria = EmisionDateCriteriaFilterParamsDTO(initialDate, finalDate)
            return this
        }

        fun withAmountCriteria(lowerBound: Float, upperBound: Float): TicketFilterParamsDTOBuilder {
            this.amountCriteria = AmountCriteriaFilterParamsDTO(lowerBound, upperBound)
            return this
        }

        fun build(): TicketFilterParamsDTO {
            return TicketFilterParamsDTO(storeCriteria, emisionDateCriteria, amountCriteria)
        }
    }
}

