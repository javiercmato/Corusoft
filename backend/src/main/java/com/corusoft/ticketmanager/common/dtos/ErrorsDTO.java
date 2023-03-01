package com.corusoft.ticketmanager.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorsDTO {
    /**
     * Error global en la aplicación
     */
    private String globalError;

    /**
     * Lista con los campos que produjeron el error
     */
    @JsonInclude(Include.NON_NULL)
    private List<FieldErrorDTO> fieldErrors;

    public ErrorsDTO(String globalError) {
        this.globalError = globalError;
    }

    public ErrorsDTO(List<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
