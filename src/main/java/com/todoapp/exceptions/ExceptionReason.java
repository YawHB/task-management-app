package com.todoapp.exceptions;

public enum ExceptionReason {

    ORG_NOT_FOUND_BY_ID("Organisation not found by this id"),
    USER_NOT_FOUND_BY_EMAIL("User not found by this email"),
    USER_ALREADY_EXIST_IN_ORG("The user with specified user id already exist in organisation with specified organisation id");

    private String value;

    ExceptionReason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
