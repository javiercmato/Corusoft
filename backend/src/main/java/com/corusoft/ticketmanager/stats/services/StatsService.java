package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;

import java.time.Month;
import java.time.YearMonth;
import java.util.Map;

public interface StatsService {

    Map<YearMonth, Double> getSpendingsByUser(Long userID) throws EntityNotFoundException;

    Map<CustomizedCategory, Double> getSpendingsThisMonth(Long userID) throws  EntityNotFoundException;

    Map<CustomizedCategory, Double> getPercentagePerCategoryThisMonth(Long userID) throws EntityNotFoundException;

    Map<YearMonth, Double> getWastesPerCategory(Long userID, Long categoryID) throws EntityNotFoundException;

}
