package com.corusoft.ticketmanager.users.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.users.entities.User;

public interface UserService {
    User signUp(User user) throws EntityAlreadyExistsException;

}
