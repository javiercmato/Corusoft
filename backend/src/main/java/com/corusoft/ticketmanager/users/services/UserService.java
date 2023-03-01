package com.corusoft.ticketmanager.users.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.exceptions.IncorrectLoginException;

public interface UserService {
    User signUp(User user) throws EntityAlreadyExistsException;

    User login(String nickname, String rawPassword) throws IncorrectLoginException;

    User loginFromToken(Long userID) throws EntityNotFoundException;
}
