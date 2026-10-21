package com.todoapp.exceptionHandling.exceptions;

public class OrganisationNotFoundException extends RuntimeException {

    public OrganisationNotFoundException(String message){
        super(message);
    }
}
