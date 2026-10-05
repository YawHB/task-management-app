package com.todoapp.task.internal.service;

import com.task_management_app.api.model.AddTaskDTO;
import com.task_management_app.api.model.TaskDTO;
import com.todoapp.exceptions.*;
import com.todoapp.task.internal.model.Task;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.task.facade.TaskDTOConverter;
import com.todoapp.task.facade.TaskFacade;
import com.todoapp.task.internal.repository.TaskRepository;
import com.todoapp.user.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskRepository taskRepository;
    private final TaskDTOConverter taskDTOConverter;
    private final ProjectFacade projectFacade;
    private final UserFacade userFacade;


    public TaskDTO addTask(AddTaskDTO addTaskDTO){

        log.info("[TaskFacadeImpl - addTask] - calling validatePostRequest");
        validatePostRequest(addTaskDTO);
        log.info("[TaskFacadeImpl - addTask] - calling toEntity");
       Task task = taskDTOConverter.toEntity(addTaskDTO);
        log.info("[TaskFacadeImpl - addTask] -  calling repo to save task");
        Task newTask = taskRepository.save(task);
        log.info("[TaskFacadeImpl - addTask] - calling toDTO");
        return taskDTOConverter.toDTO(newTask);
    }


    @Override
    public boolean existById(UUID id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.isPresent();
    }

    @Override
    public boolean taskExistInProject(UUID taskId, UUID projectId) {
        return taskRepository.existByIdAndProjectId(taskId, projectId);
    }

    private void validatePostRequest(AddTaskDTO requestBody) throws RuntimeException {
        UUID assigneeId = requestBody.getAssigneeId();
        UUID projectId = requestBody.getProjectId();
        UUID parentTaskId = requestBody.getParentTaskId();

        log.info("[TaskFacadeImpl - validatePostRequest] - checking project id");
        if(!projectFacade.existById(projectId))
            throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND.getValue());

        log.info("[TaskFacadeImpl - validatePostRequest] - finding parent task in project if exist");
        if( parentTaskId != null && !this.taskExistInProject(parentTaskId,projectId ))
            throw new TaskNotFoundInProjectException(ExceptionReason.TASK_NOT_FOUND_IN_PROJECT.getValue());

        log.info("[TaskFacadeImpl - validatePostRequest] - checking if user exist");
        if(assigneeId != null && !userFacade.existById(assigneeId))
            throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue());

        log.info("[TaskFacadeImpl - validatePostRequest] - checking if user is member of project");
        if( assigneeId != null && !userFacade.userExistInProject(assigneeId, projectId))
            throw new UserNotMemberOfProjectException(ExceptionReason.USER_NOT_MEMBER_OF_PROJECT.getValue());

        log.info("[TaskFacadeImpl - validatePostRequest] - checking if due date is after creation");
        if(requestBody.getDueDate() != null && requestBody.getDueDate().isBefore(LocalDate.now()))
            throw new InvalidDateRangeException(ExceptionReason.INVALID_DATE_RANGE.getValue());
    }
}
