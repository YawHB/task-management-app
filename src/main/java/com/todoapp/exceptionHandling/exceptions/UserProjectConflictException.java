package com.todoapp.exceptionHandling.exceptions;

public class UserProjectConflictException extends RuntimeException {
    public UserProjectConflictException(String message) {
        super(message);
    }
}

