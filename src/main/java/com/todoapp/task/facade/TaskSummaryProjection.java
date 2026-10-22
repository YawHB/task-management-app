package com.todoapp.task.facade;

import com.todoapp.task.internal.Priority;
import com.todoapp.task.internal.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskSummaryProjection {
    UUID getId();
    String getTitle();
    Status getStatus();
    Priority getPriority();
    LocalDateTime getCreatedAt();
    LocalDate getDueDate();
    String getFirstName();
    String getLastName();
    UUID getProjectId();


}
