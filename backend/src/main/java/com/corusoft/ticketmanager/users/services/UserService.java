package com.corusoft.ticketmanager.users.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.users.entities.Subscription;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.exceptions.IncorrectLoginException;
import com.corusoft.ticketmanager.users.exceptions.UserAlreadySubscribedException;

public interface UserService {
    User signUp(User user) throws EntityAlreadyExistsException;

    User login(String nickname, String rawPassword) throws IncorrectLoginException;

    User loginFromToken(Long userID) throws EntityNotFoundException;

    Subscription subscribeToPremium(Long userID) throws UserAlreadySubscribedException, EntityNotFoundException;

    User findByNameOrNickname(String query) throws EntityNotFoundException;
}
