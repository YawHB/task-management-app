package com.todoapp.user.facade;

import java.util.UUID;

public interface OrganisationProjection {

    UUID getOrganisationId();
    String getOrganisationName();
    UUID getCreatorId();
}
