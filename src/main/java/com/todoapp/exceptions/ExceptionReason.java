package com.todoapp.exceptions;

public enum ExceptionReason {

    ORG_NOT_FOUND_BY_ID("Organisation not found by this id");

    private String value;

    ExceptionReason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
