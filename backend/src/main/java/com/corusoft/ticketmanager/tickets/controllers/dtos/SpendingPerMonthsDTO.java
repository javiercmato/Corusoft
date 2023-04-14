package com.corusoft.ticketmanager.tickets.controllers.dtos;

import lombok.Data;

@Data
public class SpendingPerMonthsDTO {

    private String nameOfMonth;

    private Float spendAmount;

    public SpendingPerMonthsDTO(String nameOfMonth, Float spendAmount) {
        this.nameOfMonth = nameOfMonth;
        this.spendAmount = spendAmount;
    }

    public SpendingPerMonthsDTO(String nameOfMonth) {
        this.nameOfMonth = nameOfMonth;
    }

    public SpendingPerMonthsDTO() {}
}
