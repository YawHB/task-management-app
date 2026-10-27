package com.todoapp.task.internal.service;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.task.facade.*;
import com.todoapp.task.internal.mapper.TaskMapper;
import com.todoapp.task.internal.model.Task;
import com.todoapp.task.internal.repository.TaskRepository;
import com.todoapp.task.internal.utils.TaskValidationHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskValidationHandler validationHandler;


    public TaskDTO addTask(AddTaskDTO addTaskDTO){

        log.info("[TaskFacadeImpl - addTask] - calling validatePostRequest");
        validationHandler.validatePostRequest(addTaskDTO);
        log.info("[TaskFacadeImpl - addTask] - calling toEntity");
       Task task = taskMapper.toEntity(addTaskDTO);
        log.info("[TaskFacadeImpl - addTask] -  calling repo to save task");
        Task newTask = taskRepository.save(task);
        log.info("[TaskFacadeImpl - addTask] - calling toDTO");
        return taskMapper.toDTO(newTask);
    }

    public TaskDTO getTaskByIdInProject(UUID projectId, UUID taskId) {
        validationHandler.validateProjectExists(projectId);
        log.info("[TaskFacadeImpl - getTaskInProject] - calling taskRepo to fetch task");
        Optional<Task> task =  taskRepository.findByIdAndProjectId(taskId, projectId);
        validationHandler.validateTaskFound(task);
        log.info("[TaskFacadeImpl - getTaskInProject] - calling taskMapper and return fetch task to controller");
        return taskMapper.toDTO(task.get());
    }

    public List<TaskSummaryDTO> getTasksByProject(UUID projectId, String sortBy, String sortOrder) {
        validationHandler.validateProjectExists(projectId);
        log.info("[TaskFacadeImpl - getTasksByProject] - converting the string direction (asc/desc) to an ENUM ASC/DESC");
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        log.info("[TaskFacadeImpl - getTasksByProject] - creating a sort object, with direction and property to sort by");
        Sort sort = Sort.by((direction), toSnakeCase(sortBy));
        log.info("[TaskFacadeImpl - getTasksByProject] - calling taskRepository to query the result");
        List<TaskSummaryProjection> tasks = taskRepository.findTasksInProject(projectId, sort);
        log.info("[TaskFacadeImpl - getTasksByProject] - converts the list of fetched Tasks to taskDTOs");
        return taskMapper.toTaskSummaryDTO(tasks);
    }

    @Transactional
    public TaskDetailsDTO updateTaskAssignee(UUID projectId, UUID taskId, UpdateTaskAssigneeDTO updateTaskAssigneeDTO) {
      UUID assigneeId = updateTaskAssigneeDTO.getAssigneeId();

      validationHandler.validateUpdateAssignee(projectId, taskId, assigneeId);
        log.info("[TaskFacadeImpl - updateTaskAssignee] - calling task repository to update task ");
        taskRepository.updateTaskAssignee(projectId, taskId, assigneeId);
        log.info("[TaskFacadeImpl - updateTaskAssignee] - fetching newly update task from repository");
        Optional<TaskDetailsProjection> taskDetails =  taskRepository.findTaskDetailsByIdAndProjectId(taskId, projectId, assigneeId);
        validationHandler.validateTaskFound(taskDetails);
        log.info("[TaskFacadeImpl - updateTaskAssignee] - calling taskDetailsDTO");
        return taskMapper.taskDetailsDTO(taskDetails.get());
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
