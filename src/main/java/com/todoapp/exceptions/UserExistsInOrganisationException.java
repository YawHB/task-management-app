package com.todoapp.exceptions;

public class UserExistsInOrganisationException extends RuntimeException {
    public UserExistsInOrganisationException(String message) {
        super(message);
    }
}
