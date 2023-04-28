package com.capgemini.bedland.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Generic business exception indicating that some resource could not be found.
 * Will resolve to the {@link HttpStatus#NOT_FOUND}.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(Long id){
        super("Element with ID = " + id + " does not exist");
    }

}
