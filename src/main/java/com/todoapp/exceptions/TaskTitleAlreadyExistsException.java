package com.todoapp.exceptions;

public class TaskTitleAlreadyExistsException extends RuntimeException {
    public TaskTitleAlreadyExistsException(String message) {
        super(message);
    }
}
