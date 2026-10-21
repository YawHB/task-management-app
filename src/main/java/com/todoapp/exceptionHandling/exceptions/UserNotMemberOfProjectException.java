package com.todoapp.exceptionHandling.exceptions;

public class UserNotMemberOfProjectException extends RuntimeException {
    public UserNotMemberOfProjectException(String message) {
        super(message);
    }
}
