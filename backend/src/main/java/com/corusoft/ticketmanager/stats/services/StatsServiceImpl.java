package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
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
        Set<Ticket> tickets = user.getTickets();

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
    public Map<Category, Double> getSpendingsThisMonth(Long userID) throws EntityNotFoundException {

        User user = userUtils.fetchUserByID(userID);
        LocalDateTime thisMonth = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                .withNano(0).withDayOfMonth(1);
        List<Ticket> tickets = ticketRepo.getTicketsthisMonth(user, thisMonth);

        Map<Category, Double> spendingsThisMonth = new HashMap<>();

        if (!tickets.isEmpty()) {
            // Agrupar tickets por mes
            spendingsThisMonth = tickets.stream()
                    .collect(
                            Collectors.groupingBy(ticket -> ticket.getCustomizedCategory().getCategory(),
                                    TreeMap::new,
                                    Collectors.summingDouble(ticket ->
                                            roundDoubleWithTwoDecimals(ticket.getAmount().doubleValue())
                                    )
                            )
                    );

            // Redondear números
            spendingsThisMonth.replaceAll((key, value) -> roundDoubleWithTwoDecimals(value));
        }


        return spendingsThisMonth;
    }

    @Override
    public Map<Category, Double> getPercentagePerCategoryThisMonth(Long userID) throws EntityNotFoundException {

        User user = userUtils.fetchUserByID(userID);
        Map<Category, Double> spendings = getSpendingsThisMonth(userID);
        Map<Category, Double> percentagePerCategory = new HashMap<>();

        if (!spendings.isEmpty()) {

            Double totalGastado = spendings.values().stream().reduce((double) 0, Double::sum);
            percentagePerCategory = spendings.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> BigDecimal.valueOf(e.getValue())
                                    .divide(BigDecimal.valueOf(totalGastado), 2, RoundingMode.HALF_DOWN)
                                    .doubleValue()
                    ));
        }

        return percentagePerCategory;
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
        }

        return spendsPerCategoryMonth;

    }

    /* ******************** FUNCIONES AUXILIARES ******************** */
    public static Double roundDoubleWithTwoDecimals(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue();
    }

}
