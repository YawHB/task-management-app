package com.todoapp.exceptionHandling.exceptions;

public class UserExistsInOrganisationException extends RuntimeException {
    public UserExistsInOrganisationException(String message) {
        super(message);
    }
}
