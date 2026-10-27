package com.todoapp.task.internal.utils;

import com.task_management_app.api.model.AddTaskDTO;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.task.internal.repository.TaskRepository;
import com.todoapp.user.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class TaskValidationHandler {

    private final ProjectFacade projectFacade;
    private final UserFacade userFacade;
    private final TaskRepository taskRepository;

    public void validateProjectExists(UUID projectId) {
        log.info("[TaskValidationHandler - validateProjectExists] - calling projectFacade to check if project exists");

        if (!projectFacade.existsById(projectId)) {
            throw new ProjectNotFoundException(
                    ExceptionReason.PROJECT_NOT_FOUND.getValue());
        }
    }

    private void validateTaskTitleIsUnique(UUID projectId, String taskTitle) {
        log.info("[TaskFacadeImpl - validateTaskTitleIsUnique] - checking task title uniqueness");

        if (taskRepository.taskTitleExistsInProject(projectId, taskTitle)) {
            throw new TaskTitleAlreadyExistsException(
                    ExceptionReason.TASK_TITLE_ALREADY_EXIST.getValue());
        }
    }

    private void validateParentTaskExistsInProject(UUID parentTaskId, UUID projectId) {
        log.info("[TaskFacadeImpl - validateParentTaskExistsInProject] - checking parent task");

        if (parentTaskId != null && !taskRepository.existByIdAndProjectId(parentTaskId, projectId)) {
            throw new ParentTaskNotFoundException(
                    ExceptionReason.PARENT_TASK_NOT_FOUND_IN_PROJECT.getValue());
        }
    }

    private void validateAssigneeExists(UUID assigneeId) {
        log.info("[TaskFacadeImpl - validateAssigneeExists] - checking assignee");

        if (assigneeId != null && !userFacade.existById(assigneeId)) {
            throw new UserNotFoundException(
                    ExceptionReason.USER_NOT_FOUND_BY_ID.getValue());
        }
    }

    private void validateAssigneeIsMemberOfProject(UUID assigneeId, UUID projectId) {
        log.info("[TaskFacadeImpl - validateAssigneeIsMemberOfProject] - checking project membership");

        if (assigneeId != null && !userFacade.userExistInProject(assigneeId, projectId)) {
            throw new UserNotMemberOfProjectException(
                    ExceptionReason.USER_NOT_MEMBER_OF_PROJECT.getValue());
        }
    }

    private void validateDueDate(LocalDate dueDate) {
        log.info("[TaskFacadeImpl - validateDueDate] - checking due date");

        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new InvalidDateRangeException(
                    ExceptionReason.INVALID_DATE_RANGE.getValue());
        }
    }

    public void validateTaskExistsInProject(UUID projectId, UUID taskId) {
        log.info("[TaskFacadeImpl - validateTaskTitleIsUnique] - validate if task exist in project");

        if (!taskRepository.existByIdAndProjectId(taskId, projectId)){
            throw new TaskNotFoundInProjectException(ExceptionReason.TASK_NOT_FOUND_IN_PROJECT.getValue());
        }
    }

    public <T> void validateTaskFound(Optional<T> taskObj) {
        if(taskObj.isEmpty()){
            throw new TaskNotFoundInProjectException(ExceptionReason.TASK_NOT_FOUND_IN_PROJECT.getValue());
        }
    }

    public void validateUpdateAssignee(UUID projectId, UUID taskId, UUID assigneeId ){
        validateProjectExists(projectId);
        validateTaskExistsInProject(projectId, taskId);
        validateAssigneeIsMemberOfProject(assigneeId, projectId);
    }

    public void validatePostRequest(AddTaskDTO requestBody) {
        UUID assigneeId = requestBody.getAssigneeId();
        UUID projectId = requestBody.getProjectId();
        UUID parentTaskId = requestBody.getParentTaskId();

        validateProjectExists(projectId);
        validateTaskTitleIsUnique(projectId, requestBody.getTaskTitle());
        validateParentTaskExistsInProject(parentTaskId, projectId);
        validateAssigneeExists(assigneeId);
        validateAssigneeIsMemberOfProject(assigneeId, projectId);
        validateDueDate(requestBody.getDueDate());
    }
}
