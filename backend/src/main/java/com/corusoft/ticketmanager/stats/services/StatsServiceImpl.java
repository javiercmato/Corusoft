package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategoryID;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.tickets.services.utils.TicketUtils;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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


    /* ******************** CÁLCULO ESTADÍSTICAS ******************** */
    @Override
    public Map<YearMonth, Double> getSpendingsByUser(Long userID) throws EntityNotFoundException {
        // Comprobar si existe el usuario
        User user = userUtils.fetchUserByID(userID);

        // Recuperar tickets del usuario
        Set<Ticket> userTickets = user.getTickets();

        Map<YearMonth, Double> spendsByMonth = new TreeMap<>();
        if (!userTickets.isEmpty()) {
            // Agrupar tickets por mes
            spendsByMonth = userTickets.stream()
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
    public Map<CustomizedCategory, Double> getSpendingsThisMonth(Long userID) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Map<CustomizedCategory, Double> getPercentagePerCategoryThisMonth(Long userID) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Map<YearMonth, Double> getWastesPerCategory(Long userID, Long categoryID) throws EntityNotFoundException {
        // Comprobar si existe el usuario y la categoría customizada.
        User user = userUtils.fetchUserByID(userID);
        CustomizedCategory customizedCategory = ticketUtils.fetchCustomizedCategoryById(new
                CustomizedCategoryID(user.getId(), categoryID));

        List<Ticket> tickets = ticketRepo.getTicketByCategoryId(customizedCategory.getCategory().getId());
        Map<YearMonth, Double> spendsPerCategoryMonth = new TreeMap<>();

        if (!tickets.isEmpty()) {

            spendsPerCategoryMonth = tickets.stream()
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
            spendsPerCategoryMonth.replaceAll((key, value) -> roundDoubleWithTwoDecimals(value));
        }

        return spendsPerCategoryMonth;

    }

    /* ******************** FUNCIONES AUXILIARES ******************** */
    public static Double roundDoubleWithTwoDecimals(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue();
    }

}
