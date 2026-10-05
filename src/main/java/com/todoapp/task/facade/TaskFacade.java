package com.todoapp.task.facade;

import java.util.UUID;

public interface TaskFacade {
     boolean existById(UUID id);

     boolean taskExistInProject(UUID taskId, UUID projectId);
}
