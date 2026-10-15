package com.todoapp.task.internal.repository;

import com.todoapp.task.internal.model.Task;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {


    @Query(value = "SELECT COUNT(*) > 0 FROM todo_tasks " +
                   "WHERE id = :taskId AND project_id = :projectId",
                    nativeQuery = true)
    boolean existByIdAndProjectId(UUID taskId, UUID projectId);

    @Query(value = "SELECT COUNT(*) > 0 FROM todo_tasks " +
                    "WHERE project_id = :projectId AND title = :taskTitle",
                    nativeQuery = true)
    boolean taskTitleExistsInProject(UUID projectId, String taskTitle);

    @Query(value = "SELECT * FROM todo_tasks " +
                    "WHERE id = :taskId "+
                    "AND project_id = :projectId",
                    nativeQuery = true)
    Optional<Task> findByIdAndProjectId(UUID projectId, UUID taskId);
}

