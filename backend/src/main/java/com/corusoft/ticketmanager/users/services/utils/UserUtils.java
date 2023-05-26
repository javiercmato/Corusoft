package com.corusoft.ticketmanager.users.services.utils;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class UserUtils {
    @Autowired
    private UserRepository userRepo;

    /**
     * Busca un usuario por el ID en la base de datos.
     *
     * @param userID ID del usuario a buscar
     * @return Usuario encontrado
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public User fetchUserByID(Long userID) throws EntityNotFoundException {
        return userRepo.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), userID));
    }

    /**
     * Busca un usuario por su nickname en la base de datos.
     *
     * @param nickname Apodo de usuario a buscar
     * @return Usuario encontrado
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public User fetchUserByNickname(String nickname) throws EntityNotFoundException {
        // Comprobar si existe el usuario con el nickname recibido
        return userRepo.findByNicknameIgnoreCase(nickname)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), nickname));
    }

    /**
     * Busca un usuario por su nombre en la base de datos.
     *
     * @param name Nombre de usuario a buscar
     * @return Usuario encontrado
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public User fetchUserByName(String name) throws EntityNotFoundException {
        // Comprobar si existe el usuario con el nickname recibido
        return userRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), name));
    }

    public User findByNameOrNickname(String queryText) throws EntityNotFoundException {
        try {
            return fetchUserByName(queryText);
        } catch (EntityNotFoundException e) {
            return fetchUserByNickname(queryText);
        }
    }




    /**
     * Comprueba si dos usuarios son el mismo comparando sus ID
     */
    public boolean doUsersMatch(Long requestUserID, Long targetUserID) {
        return requestUserID.equals(targetUserID);
    }
}
