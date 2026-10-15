package com.todoapp.exceptions;

public class InvalidStringException extends RuntimeException {
    public InvalidStringException(String message) {
        super(message);
    }
}
