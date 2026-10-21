package com.todoapp.exceptionHandling.exceptions;

public class InvalidStringException extends RuntimeException {
    public InvalidStringException(String message) {
        super(message);
    }
}
