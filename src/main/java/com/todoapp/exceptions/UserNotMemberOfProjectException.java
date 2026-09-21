package com.todoapp.exceptions;

public class UserNotMemberOfProjectException extends RuntimeException {
    public UserNotMemberOfProjectException(String message) {
        super(message);
    }
}
