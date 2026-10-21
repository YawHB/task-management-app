package com.todoapp.exceptionHandling.exceptions;

public class TaskTitleAlreadyExistsException extends RuntimeException {
    public TaskTitleAlreadyExistsException(String message) {
        super(message);
    }
}
