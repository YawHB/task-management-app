package com.todoapp.exceptions;

public class UserProjectConflictException extends RuntimeException {
    public UserProjectConflictException(String message) {
        super(message);
    }
}

