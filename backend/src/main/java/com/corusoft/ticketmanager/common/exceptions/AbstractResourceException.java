package com.corusoft.ticketmanager.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class AbstractResourceException extends Exception implements Serializable {

}
