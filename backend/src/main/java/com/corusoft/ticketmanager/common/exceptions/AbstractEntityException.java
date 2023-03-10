package com.corusoft.ticketmanager.common.exceptions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public abstract class AbstractEntityException extends Exception implements Serializable {
    @NotBlank
    private String entityName;

    @NotNull
    private Object key;

}
