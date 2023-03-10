package com.corusoft.ticketmanager.users.repositories;

import com.corusoft.ticketmanager.users.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Comprueba si existe un usuario por su nickname, ignorando mayúsculas o minúsculas
     */
    boolean existsByNicknameIgnoreCase(String nickname);

    /**
     * Recupera un usuario por su nickname, ignorando mayusculas o minúsculas
     */
    Optional<User> findByNicknameIgnoreCase(String nickname);


}
