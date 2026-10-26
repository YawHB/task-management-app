package com.todoapp.user.facade;

import java.util.UUID;

public interface UserSummaryProjection {

    UUID getId();
    String getFirstName();
    String getLastName();
    String getUserName();
    String email();

}
