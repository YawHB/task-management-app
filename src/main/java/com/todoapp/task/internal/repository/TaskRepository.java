package com.todoapp.task.internal.repository;

import com.task_management_app.api.model.TaskDetailsDTO;
import com.todoapp.task.facade.TaskDetailsProjection;
import com.todoapp.task.facade.TaskSummaryProjection;
import com.todoapp.task.internal.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(value = "SELECT t.id, t.title, t.status, t.priority, t.created_at, t.due_date, t.project_id,  " +
                    "u.first_name, u.last_name " +
                   "FROM todo_tasks AS t " +
                   "LEFT JOIN users AS u " +
                   "ON u.id = t.assignee_id " +
                   " WHERE project_id = :projectId ",
                   nativeQuery = true)
    List<TaskSummaryProjection> findTasksInProject(UUID projectId, Sort sort);

    @Modifying
    @Query(value = "UPDATE todo_tasks SET assignee_id = :assigneeId " +
                   "WHERE id = :taskId AND project_id = :projectId ",
                   nativeQuery = true)
    void updateTaskAssignee(UUID projectId, UUID taskId, UUID assigneeId);



    @Query(value = "SELECT t.id AS taskId, t.title AS taskTitle, t.description AS description, " +
                   "t.parent_task_id AS parentTaskId, t.status, t.priority AS priority, t.created_at AS createdAd,  " +
                   "t.due_date AS dueDate, t.updated_at AS updatedAt, " +
                   "p.id AS projectId, p.name AS projectName, " +
                   "u.id AS assigneeId, u.first_name AS firstName, u.last_name AS lastName, u.email AS email " +
                   "FROM todo_tasks AS t " +
                   "LEFT JOIN users AS u ON t.assignee_id = u.id " +
                   "LEFT JOIN project AS p ON t.project_id = p.id " +
                   "WHERE t.assignee_id = :assigneeId " +
                   "AND t.id = :taskId " +
                   "AND p.id = :projectId",
                   nativeQuery = true)
    Optional<TaskDetailsProjection> findTaskDetailsByIdAndProjectId(UUID taskId, UUID projectId, UUID assigneeId);
}

