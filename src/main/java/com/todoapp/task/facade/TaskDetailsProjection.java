package com.todoapp.task.facade;

import com.todoapp.task.internal.Priority;
import com.todoapp.task.internal.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskDetailsProjection {

    UUID getTaskId();
    String getTaskTitle();
    String getDescription();
    UUID getParentTaskId();

    UUID getProjectId();
    String getProjectName();

    UUID getAssigneeId();
    String getFirstName();
    String getLastName();
    String getEmail();

    Status getStatus();
    Priority getPriority();

    LocalDate getDueDate();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}


