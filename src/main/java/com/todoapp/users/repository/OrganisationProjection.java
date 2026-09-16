package com.todoapp.users.repository;

import java.util.UUID;

public interface OrganisationProjection {

    UUID getOrganisationId();
    String getOrganisationName();
}
