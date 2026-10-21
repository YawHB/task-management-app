package com.todoapp.task.internal.service;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.task.facade.TaskDetailsProjection;
import com.todoapp.task.facade.TaskSummaryProjection;
import com.todoapp.task.internal.model.Task;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.task.facade.TaskDTOConverter;
import com.todoapp.task.facade.TaskFacade;
import com.todoapp.task.internal.repository.TaskRepository;
import com.todoapp.user.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
        this.validatePostRequest(addTaskDTO);
        log.info("[TaskFacadeImpl - addTask] - calling toEntity");
       Task task = taskDTOConverter.toEntity(addTaskDTO);
        log.info("[TaskFacadeImpl - addTask] -  calling repo to save task");
        Task newTask = taskRepository.save(task);
        log.info("[TaskFacadeImpl - addTask] - calling toDTO");
        return taskDTOConverter.toDTO(newTask);
    }

    public TaskDTO getTaskByIdInProject(UUID projectId, UUID taskId) {
        log.info("[TaskFacadeImpl - getTaskInProject] - calling projectFacade to find project");
      if(!projectFacade.existsById(projectId)) {
          throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND.getValue());
      }
        log.info("[TaskFacadeImpl - getTaskInProject] - calling taskRepo to fetch task");
        Optional<Task> task =  taskRepository.findByIdAndProjectId(projectId, taskId);
        if(task.isEmpty()){
            throw new TaskNotFoundInProjectException(ExceptionReason.TASK_NOT_FOUND_IN_PROJECT.getValue());
        }

        log.info("[TaskFacadeImpl - getTaskInProject] - calling DTO converter and return fetch task to controller");
        return taskDTOConverter.toDTO(task.get());

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

    public List<TaskSummaryDTO> getTasksByProject(UUID projectId, String sortBy, String sortOrder) {
        log.info("[TaskFacadeImpl - getTasksByProject] - calling projectFacade to check if project exists");
        if(!projectFacade.existsById(projectId)) throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND.getValue());
        log.info("[TaskFacadeImpl - getTasksByProject] - converting the string direction (asc/desc) to an ENUM ASC/DESC");
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        log.info("[TaskFacadeImpl - getTasksByProject] - creating a sort object, with direction and property to sort by");
        Sort sort = Sort.by((direction), toSnakeCase(sortBy));
        log.info("[TaskFacadeImpl - getTasksByProject] - calling taskRepository to query the result");
        List<TaskSummaryProjection> tasks = taskRepository.findTasksInProject(projectId, sort);
        log.info("[TaskFacadeImpl - getTasksByProject] - converts the list of fetched Tasks to taskDTOs in taskDTOConverter ");
        return taskDTOConverter.toDTO(tasks);
    }

    @Transactional
    public TaskDetailsDTO updateTaskAssignee(UUID projectId, UUID taskId, UpdateTaskAssigneeDTO updateTaskAssigneeDTO) {
      UUID assigneeId = updateTaskAssigneeDTO.getAssigneeId();
      validateUpdateAssignee(projectId, taskId, assigneeId);

        log.info("[TaskFacadeImpl - updateTaskAssignee] - calling task repository to update task ");
        taskRepository.updateTaskAssignee(projectId, taskId, assigneeId);
        log.info("[TaskFacadeImpl - updateTaskAssignee] - fetching newly update task from repository");
        Optional<TaskDetailsProjection> taskDetails =  taskRepository.findTaskDetailsByIdAndProjectId(taskId, projectId, assigneeId);
        if(taskDetails.isEmpty()) throw new TaskNotFoundInProjectException(ExceptionReason.TASK_NOT_FOUND_IN_PROJECT.getValue());
        log.info("[TaskFacadeImpl - updateTaskAssignee] - calling toDTO and");
        return taskDTOConverter.taskDetailsDTO(taskDetails.get());
    }



    private void validatePostRequest(AddTaskDTO requestBody) throws RuntimeException {
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

    private void validateUpdateAssignee(UUID projectId, UUID taskId, UUID assigneeId ){
        validateProjectExists(projectId);
        validateTaskExistInProject(projectId, taskId);
        validateAssigneeIsMemberOfProject(assigneeId, projectId);

    }


    private void validateProjectExists(UUID projectId) {
        log.info("[TaskFacadeImpl - validateProjectExists] - checking project id");

        if (!projectFacade.existsById(projectId)) {
            throw new ProjectNotFoundException(
                    ExceptionReason.PROJECT_NOT_FOUND.getValue());
        }
    }

    private void validateTaskExistInProject(UUID projectId, UUID taskId) {
        log.info("[TaskFacadeImpl - validateTaskTitleIsUnique] - validate if task exist in project");

        if (!taskRepository.existByIdAndProjectId(taskId, projectId)){
            throw new TaskNotFoundInProjectException(ExceptionReason.TASK_NOT_FOUND_IN_PROJECT.getValue());
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

        if (parentTaskId != null && !taskExistInProject(parentTaskId, projectId)) {
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


    private String toSnakeCase(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);

            if (Character.isUpperCase(current)) {
                result.append('_');
                result.append(Character.toLowerCase(current));
            } else {
                result.append(current);
            }
        }

        return result.toString();
    }
}
