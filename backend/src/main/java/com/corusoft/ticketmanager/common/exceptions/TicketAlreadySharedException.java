package com.corusoft.ticketmanager.common.exceptions;

public class TicketAlreadySharedException extends AbstractEntityException {

    public TicketAlreadySharedException(String entityName, Object key) { super(entityName, key);}
}
