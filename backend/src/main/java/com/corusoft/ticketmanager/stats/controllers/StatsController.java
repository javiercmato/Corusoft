package com.corusoft.ticketmanager.stats.controllers;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.stats.services.StatsService;
import com.corusoft.ticketmanager.tickets.controllers.dtos.WastesPerCategoryParams;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.CategoryDto;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.services.TicketService;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private StatsService statsService;


    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */


    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */


    /* ******************** ENDPOINTS ******************** */
    @GetMapping(path = "/spendingsPerMonth",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Double> getSpendingsPerMonth(@RequestAttribute("userID") Long userID)
            throws EntityNotFoundException {

        return convertMapKeyToString(statsService.getSpendingsByUser(userID));
    }

    @PostMapping(path = "/wastesCategory",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<YearMonth, Double> getWastesPerCategory(@RequestAttribute("userID") Long userID,
                                                       @RequestBody @Validated WastesPerCategoryParams wastesPerCategoryParams)
            throws EntityNotFoundException {

        return statsService.getWastesPerCategory(userID, wastesPerCategoryParams.getCategoryId());
    }

    @GetMapping(path = "/spendingsThisMonth",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Double> getSpendingsThisMonth(@RequestAttribute("userID") Long userID)
            throws EntityNotFoundException {

        return convertMapKeyToString(statsService.getSpendingsThisMonth(userID));
    }

    @GetMapping(path = "/percentagePerCategory",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Double> getPercentagePerCategoryThisMonth(@RequestAttribute("userID") Long userID)
            throws EntityNotFoundException {

        return convertMapKeyToString(statsService.getPercentagePerCategoryThisMonth(userID));
    }


    private static Map<String, Double> convertMapKeyToString(Map<?, Double> originalMap) {
        return originalMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue
                ));
    }
}
