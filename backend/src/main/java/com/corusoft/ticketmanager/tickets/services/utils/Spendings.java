package com.corusoft.ticketmanager.tickets.services.utils;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.Month;

public class Spendings {

    private LocalDateTime date;

    private Float spendings;

    public Spendings() {}

    public Spendings(LocalDateTime month, Float spendings) {
        this.date = month;
        this.spendings = spendings;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Float getSpendings() {
        return spendings;
    }

    public void setSpendings(Float spendings) {
        this.spendings = spendings;
    }
}
