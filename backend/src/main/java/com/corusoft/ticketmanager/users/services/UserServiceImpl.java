package com.corusoft.ticketmanager.users.services;

import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.users.entities.*;
import com.corusoft.ticketmanager.users.exceptions.IncorrectLoginException;
import com.corusoft.ticketmanager.users.exceptions.UserAlreadySubscribedException;
import com.corusoft.ticketmanager.users.repositories.SubscriptionRepository;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Value("${project.constants.subscription_length_in_days}")
    private long subscriptionLengthInDays;

    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private SubscriptionRepository subsRepo;


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

    @Transactional(readOnly = true)
    @Override
    public User login(String nickname, String rawPassword) throws IncorrectLoginException {
        // Comprobar si el usuario existe
        User user;
        try {
            user = userUtils.fetchUserByNickname(nickname);
        } catch (EntityNotFoundException ex) {
            throw new IncorrectLoginException();
        }

        // Comprobar si las contraseñas coinciden
        if (!passwordEncoder.matches(rawPassword, user.getPassword()))
            throw new IncorrectLoginException();

        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public User loginFromToken(Long userID) throws EntityNotFoundException {
        return userUtils.fetchUserByID(userID);
    }

    @Override
    public Subscription subscribeToPremium(Long userID) throws UserAlreadySubscribedException, EntityNotFoundException {
        // Recuperar al usuario
        User user = userUtils.fetchUserByID(userID);

        // Comprobar que no tenga subscripción activa actualmente
        if (user.hasSubscriptionActive())
            throw new UserAlreadySubscribedException();

        // Crear la subscripción
        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = new Subscription();
        subscription.setRegisteredAt(now);
        subscription.setEndingAt(now.plusDays(subscriptionLengthInDays));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        user.assignSubscription(subscription);

        // Guardar datos y devolver subscripción
        return subsRepo.save(subscription);
    }

}
