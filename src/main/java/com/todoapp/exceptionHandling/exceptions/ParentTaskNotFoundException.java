package com.todoapp.exceptionHandling.exceptions;

public class ParentTaskNotFoundException extends RuntimeException {
    public ParentTaskNotFoundException(String message) {
        super(message);
    }
}
