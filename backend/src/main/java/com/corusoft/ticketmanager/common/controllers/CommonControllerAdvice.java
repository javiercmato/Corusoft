package com.corusoft.ticketmanager.common.controllers;

import com.corusoft.ticketmanager.common.dtos.ErrorsDTO;
import com.corusoft.ticketmanager.common.dtos.FieldErrorDTO;
import com.corusoft.ticketmanager.common.exceptions.EntityAlreadyExistsException;
import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.exceptions.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class CommonControllerAdvice {
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    // Referencias a los errores en los ficheros de i18n
    public static final String ENTITY_ALREADY_EXISTS_EXCEPTION_KEY = "common.exceptions.EntityAlreadyExistsException";
    public static final String ENTITY_NOT_FOUND_EXCEPTION_KEY = "common.exceptions.EntityNotFoundException";
    public static final String PERMISSION_EXCEPTION_KEY = "common.exceptions.PermissionException";
    @Autowired
    private MessageSource messageSource;

    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleEntityAlreadyExistsException(EntityAlreadyExistsException exception, Locale locale) {
        String exceptionMessage = messageSource.getMessage(
                exception.getEntityName(), null, exception.getEntityName(), locale
        );
        String globalErrorMessage = messageSource.getMessage(
                ENTITY_ALREADY_EXISTS_EXCEPTION_KEY,
                new Object[]{exceptionMessage, exception.getKey().toString()},
                ENTITY_ALREADY_EXISTS_EXCEPTION_KEY,
                locale
        );

        return new ErrorsDTO(globalErrorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)     // 404
    @ResponseBody
    public ErrorsDTO handleEntityNotFoundException(EntityNotFoundException exception, Locale locale) {
        String exceptionMessage = messageSource.getMessage(
                exception.getEntityName(), null, exception.getEntityName(), locale
        );
        String globalErrorMessage = messageSource.getMessage(
                ENTITY_NOT_FOUND_EXCEPTION_KEY,
                new Object[]{exceptionMessage, exception.getKey().toString()},
                ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale
        );

        return new ErrorsDTO(globalErrorMessage);
    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)     // 403
    @ResponseBody
    public ErrorsDTO handlePermissionException(PermissionException exception, Locale locale) {
        String globalErrorMessage = messageSource.getMessage(
                PERMISSION_EXCEPTION_KEY, null, PERMISSION_EXCEPTION_KEY, locale
        );

        return new ErrorsDTO(globalErrorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldErrorDTO> fieldErrorDTOS = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((field) -> new FieldErrorDTO(field.getField(), field.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorsDTO(fieldErrorDTOS);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleServletRequestBindingException(ServletRequestBindingException exception, Locale locale) {
        String globalErrorMessage = messageSource.getMessage(
                PERMISSION_EXCEPTION_KEY, null, PERMISSION_EXCEPTION_KEY, locale
        );

        return new ErrorsDTO(globalErrorMessage);
    }

}
