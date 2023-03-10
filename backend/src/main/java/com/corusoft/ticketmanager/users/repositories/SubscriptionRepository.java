package com.corusoft.ticketmanager.users.repositories;

import com.corusoft.ticketmanager.users.entities.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long>,
        CrudRepository<Subscription, Long> {

}
