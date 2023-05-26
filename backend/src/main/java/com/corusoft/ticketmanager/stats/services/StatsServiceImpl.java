package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.CategoryDto;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import com.corusoft.ticketmanager.tickets.repositories.CategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.tickets.services.utils.TicketUtils;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private TicketRepository ticketRepo;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TicketUtils ticketUtils;
    @Autowired
    private CategoryRepository categoryRepository;


    /* ******************** CÁLCULO ESTADÍSTICAS ******************** */
    @Override
    public Map<YearMonth, Double> getSpendingsByUser(Long userID) throws EntityNotFoundException {
        // Comprobar si existe el usuario
        User user = userUtils.fetchUserByID(userID);

        // Recuperar tickets del usuario
        Set<Ticket> tickets = ticketRepo.getTicketsByUser(user.getId());

        Map<YearMonth, Double> spendsByMonth = new TreeMap<>();

        if (!tickets.isEmpty()) {
            // Agrupar tickets por mes
            spendsByMonth = tickets.stream()
                    .collect(
                        // Agrupar tickets por año y mes de emisión
                        Collectors.groupingBy(ticket -> YearMonth.from(ticket.getEmittedAt()),
                        // Insertándolos en un Map
                        TreeMap::new,
                        // Para cada grupo, sumar la cantidad de todos sus tickets
                        Collectors.summingDouble(ticket ->
                                roundDoubleWithTwoDecimals(ticket.getAmount().doubleValue())
                        )
                    ));

            // Redondear números
            spendsByMonth.replaceAll((key, value) -> roundDoubleWithTwoDecimals(value));
        }

        return spendsByMonth;
    }

    @Override
    public Map<String, Double> getSpendingsThisMonth(Long userID) throws EntityNotFoundException {

        User user = userUtils.fetchUserByID(userID);
        LocalDateTime thisMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Ticket> tickets = ticketRepo.getTicketsthisMonth(user, thisMonth);
        Map<String, Double> results = new HashMap<>();

        for (Ticket ticket : tickets) {
            Category category = ticket.getCustomizedCategory().getCategory();
            Double amount = roundDoubleWithTwoDecimals(Double.valueOf(ticket.getAmount()));
            results.merge(category.getName(), amount, Double::sum);
        }

        return results;
    }

    @Override
    public Map<String, Double> getPercentagePerCategoryThisMonth(Long userID) throws EntityNotFoundException {
        User user = userUtils.fetchUserByID(userID);
        LocalDateTime thisMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Ticket> tickets = ticketRepo.getTicketsthisMonth(user, thisMonth);
        Map<String, Double> results = new HashMap<>();
        double totalAmount = 0.0;

        for (Ticket ticket : tickets) {
            Category category = ticket.getCustomizedCategory().getCategory();
            double amount = roundDoubleWithTwoDecimals(Double.valueOf(ticket.getAmount()));
            totalAmount += amount;
            results.merge(category.getName(), amount, Double::sum);
        }

        for (Map.Entry<String, Double> entry : results.entrySet()) {
            double percentage = (entry.getValue() / totalAmount) * 100.0;
            double roundedPercentage = roundDoubleWithTwoDecimals(percentage);
            entry.setValue(roundedPercentage);
        }

        return results;
    }

    @Override
    public Map<YearMonth, Double> getWastesPerCategory(Long userID, Long categoryID) throws EntityNotFoundException {

        User user = userUtils.fetchUserByID(userID);
        Category category = ticketUtils.fetchCategoryById(categoryID);

        List<Ticket> tickets = ticketRepo.getTicketByCategoryId(categoryID);
        Map<YearMonth, Double> spendsPerCategoryMonth = new TreeMap<>();

        if (!tickets.isEmpty()) {
            spendsPerCategoryMonth = tickets.stream()
                    .collect(
                            Collectors.groupingBy(ticket -> YearMonth.from(ticket.getEmittedAt()),
                                    TreeMap::new,
                                    Collectors.summingDouble(ticket ->
                                            roundDoubleWithTwoDecimals(ticket.getAmount().doubleValue())
                                    )
                            ));

            // Redondear números
            spendsPerCategoryMonth.replaceAll((key, value) -> roundDoubleWithTwoDecimals(value));
        } else {
            spendsPerCategoryMonth = Collections.emptyMap();
        }

        return spendsPerCategoryMonth;

    }

    /* ******************** FUNCIONES AUXILIARES ******************** */
    public static Double roundDoubleWithTwoDecimals(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue();
    }

}
