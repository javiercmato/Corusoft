package com.corusoft.ticketmanager.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtData {
    private Long userID;
    private String nickname;
    private String role;
}
