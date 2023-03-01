package com.corusoft.ticketmanager.common.exceptions;

public class EntityAlreadyExistsException extends AbstractEntityException {
    public EntityAlreadyExistsException(String entityName, Object key) {
        super(entityName, key);
    }
}
