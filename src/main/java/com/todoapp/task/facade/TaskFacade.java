package com.todoapp.task.facade;

import java.util.UUID;

public interface TaskFacade {
     /**
      * Checks whether a project exists with the specified ID.
      *
      * @param id the UUID of the project to check
      * @return {@code true} if the project exists, {@code false} otherwise
      */
     boolean existById(UUID id);

     /**
      * Checks whether a task exists within the specified project.
      *
      * @param taskId the UUID of the task to check
      * @param projectId the UUID of the project to check
      * @return {@code true} if the task exists within the project, {@code false} otherwise
      */
     boolean taskExistInProject(UUID taskId, UUID projectId);
}
