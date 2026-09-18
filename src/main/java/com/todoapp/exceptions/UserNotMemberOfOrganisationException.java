package com.todoapp.exceptions;

public class UserNotMemberOfOrganisationException extends RuntimeException {
    public UserNotMemberOfOrganisationException(String message) {
        super(message);
    }
}
