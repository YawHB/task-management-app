package com.todoapp.project.facade;

import java.util.UUID;

public interface ProjectSummaryProjection {
    UUID getProjectId();
    String getName();
}
