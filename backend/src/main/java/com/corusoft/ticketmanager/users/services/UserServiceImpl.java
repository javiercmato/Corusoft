package com.corusoft.ticketmanager.users.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.entities.UserRole;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    /* ******************** FUNCIONALIDADES USUARIO ******************** */
    @Override
    public User signUp(User user) throws EntityAlreadyExistsException {
        // Comprobar si existe un usuario con el mismo nickname
        if (userRepo.existsByNicknameIgnoreCase(user.getNickname()))
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), user.getNickname());

        // Asignar datos por defecto del usuario: contraseña cifrada, rol, fecha registro, etc
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRegistered_at(LocalDateTime.now());
        user.setRole(UserRole.USER);

        // Guardar datos y devolver usuario creado
        return userRepo.save(user);
    }


    /* ******************** FUNCIONES AUXILIARES ******************** */

}
