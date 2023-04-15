package com.corusoft.ticketmanager.stats.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.tickets.entities.Ticket;
import com.corusoft.ticketmanager.tickets.repositories.TicketRepository;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    /* ******************** FUNCIONES AUXILIARES ******************** */
    public static Double roundDoubleWithTwoDecimals(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue();
    }

}
