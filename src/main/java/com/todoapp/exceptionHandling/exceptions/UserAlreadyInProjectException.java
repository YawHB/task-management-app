package com.todoapp.exceptionHandling.exceptions;

public class UserAlreadyInProjectException extends RuntimeException {
    public UserAlreadyInProjectException(String message) {
        super(message);
    }
}
