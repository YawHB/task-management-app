package com.todoapp.project;

import java.util.UUID;

public interface ProjectSummaryProjection {
    UUID getProjectId();
    String getName();
}
