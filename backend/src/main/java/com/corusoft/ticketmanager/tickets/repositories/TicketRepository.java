package com.corusoft.ticketmanager.tickets.repositories;

import com.corusoft.ticketmanager.tickets.entities.Ticket;
import com.corusoft.ticketmanager.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TicketRepository extends ListCrudRepository<Ticket, Long>,
        ListPagingAndSortingRepository<Ticket, Long>,
        JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE " +
            "t.customizedCategory.category.id = ?1 ")
    List<Ticket> getTicketByCategoryId(Long categoryID);

    @Query("SELECT t FROM Ticket t WHERE" +
            " t.creator = ?1 AND " +
            " t.registeredAt > ?2 ")
    List<Ticket> getTicketsthisMonth(User user, LocalDateTime thisMonth);

    @Query("SELECT t from Ticket t WHERE t.creator.id = ?1")
    Set<Ticket> getTicketsByUser(Long userId);
    List<Ticket> findDistinctByCreatorOrderByEmittedAtDesc(User creator);

}
