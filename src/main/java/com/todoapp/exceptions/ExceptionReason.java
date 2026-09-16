package com.todoapp.exceptions;

public enum ExceptionReason {

    ORG_NOT_FOUND_BY_ID("Organisation not found by this id"),
    USER_NOT_FOUND_BY_EMAIL("User not found by this email"),
    USER_ALREADY_EXIST_IN_ORG("The user with specified user id already exist in organisation with specified organisation id"),
    PROJECT_NOT_FOUND_IN_ORG("Project not found by this name in the specified organisation"),
    USER_ALREADY_MEMBER_OF_PROJECT("User already part of this project"),
    USER_NOT_FOUND_BY_ID("User not found by this id");


    private String value;

    ExceptionReason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
