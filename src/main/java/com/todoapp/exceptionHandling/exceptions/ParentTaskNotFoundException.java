package com.todoapp.exceptions;

public class ParentTaskNotFoundException extends RuntimeException {
    public ParentTaskNotFoundException(String message) {
        super(message);
    }
}
