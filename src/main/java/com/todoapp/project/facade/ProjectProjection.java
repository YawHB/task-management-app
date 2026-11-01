package com.todoapp.project.facade;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ProjectProjection {

        UUID getProjectId();
        UUID getOrganisationId();
        String getProjectName();
        LocalDateTime getCreatedAt();

        UUID getUserId();
        String getFirstName();
        String getLastName();
        String getEmail();
        String getUsername();
}
