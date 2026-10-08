package com.todoapp.exceptionHandling.exceptions;

public class UserNotMemberOfOrganisationException extends RuntimeException {
    public UserNotMemberOfOrganisationException(String message) {
        super(message);
    }
}
