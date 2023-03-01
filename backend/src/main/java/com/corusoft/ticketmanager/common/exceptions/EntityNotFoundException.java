package com.corusoft.ticketmanager.common.exceptions;

public class EntityNotFoundException extends AbstractEntityException {
    public EntityNotFoundException(String entityName, Object key) {
        super(entityName, key);
    }
}
