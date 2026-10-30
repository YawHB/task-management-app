package com.todoapp.exceptionHandling.exceptions;

public class DuplicateOrganisationNameException extends RuntimeException {
    public DuplicateOrganisationNameException(String message) {
        super(message);
    }
}
