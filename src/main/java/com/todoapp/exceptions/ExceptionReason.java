package com.todoapp.exceptions;

public enum ExceptionReason {

    ORG_NOT_FOUND_BY_ID("Organisation not found by this id"),
    USER_NOT_MEMBER_OF_ORG("Access denied - the selected user is not a member of this organisation");


    private String value;

    ExceptionReason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
