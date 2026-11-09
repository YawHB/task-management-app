package com.todoapp.organisation.facade;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrganisationProjectProjection {
    UUID getProjectId();
    String getProjectName();
    UUID getOrganisationId();
    LocalDateTime getCreatedAt();
}
