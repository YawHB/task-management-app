package com.todoapp.users.facade;

import java.util.UUID;

public interface OrganisationProjection {

    UUID getOrganisationId();
    String getOrganisationName();
    UUID getCreatorId();
}
