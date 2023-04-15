package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;

import java.time.YearMonth;
import java.util.Map;

public interface StatsService {

    Map<YearMonth, Double> getSpendingsByUser(Long userID) throws EntityNotFoundException;

}
