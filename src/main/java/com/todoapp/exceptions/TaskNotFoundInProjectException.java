package com.todoapp.exceptions;

public class TaskNotFoundInProjectException extends RuntimeException {
    public TaskNotFoundInProjectException(String message) {
        super(message);
    }
}
