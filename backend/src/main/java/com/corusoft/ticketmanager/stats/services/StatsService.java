package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;

import java.time.Month;
import java.time.YearMonth;
import java.util.Map;

public interface StatsService {

    Map<YearMonth, Double> getSpendingsByUser(Long userID) throws EntityNotFoundException;

    /**
     * Devuelve para un usuario todos los gastos asociados a una categoría de este mes
     * **/
    Map<Category, Double> getSpendingsThisMonth(Long userID) throws  EntityNotFoundException;

    Map<Category, Double> getPercentagePerCategoryThisMonth(Long userID) throws EntityNotFoundException;

    /**
     * Devuelve el histórico de gastos por categoría concreta en diferentes meses.
     * **/
    Map<YearMonth, Double> getWastesPerCategory(Long userID, Long categoryID) throws EntityNotFoundException;

}
