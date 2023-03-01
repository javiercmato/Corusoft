package com.corusoft.ticketmanager.users.controllers.dtos.conversors;

import com.corusoft.ticketmanager.users.controllers.dtos.AuthenticatedUserDTO;
import com.corusoft.ticketmanager.users.controllers.dtos.RegisterUserParamsDTO;
import com.corusoft.ticketmanager.users.controllers.dtos.UserDTO;
import com.corusoft.ticketmanager.users.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConversor {
    /* ******************** Convertir a DTO ******************** */
    public static UserDTO toUserDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserID(entity.getId());
        dto.setName(entity.getName());
        dto.setNickname(entity.getNickname());
        dto.setEmail(entity.getEmail());
        dto.setSubscribed(entity.hasSubscriptionActive());
        dto.setRole(entity.getRole().toString());
        dto.setRegisteredAt(entity.getRegistered_at());

        return dto;
    }

    public static AuthenticatedUserDTO toAuthenticatedUserDTO(User entity, String token) {
        return new AuthenticatedUserDTO(token, toUserDTO(entity));
    }

    /* ******************** Convertir a Entidad ******************** */
    public static User fromRegisterUserParamsDTO(RegisterUserParamsDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setNickname(dto.getNickname());
        entity.setPassword(dto.getRawPassword());
        entity.setEmail(dto.getEmail());

        return entity;
    }

}
