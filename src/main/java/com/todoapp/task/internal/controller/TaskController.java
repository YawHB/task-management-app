package com.todoapp.task.internal.controller;

import com.task_management_app.api.TaskApi;
import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.task.internal.service.TaskFacadeImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.exceptionHandling.exceptions.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController // RestController = Controller + ResponseBody
@AllArgsConstructor
public class TaskController implements TaskApi {

    private final TaskFacadeImpl taskFacadeImpl;

    @Override
    public ResponseEntity<TaskDTO> addTask(AddTaskDTO addTaskDTO) {
            log.info("[TaskController - addTask] - calling facade");
        TaskDTO taskDTO = taskFacadeImpl.addTask(addTaskDTO);
            log.info("[TaskController - adTask] - returning created task to client");
        return  ResponseEntity.status(HttpStatus.OK).body(taskDTO);
    }

    public ResponseEntity<TaskDTO> getTaskByIdInProject(UUID projectId, UUID taskId) {
        log.info("[TaskController - getTaskInProject] - calling taskFacadeImpl");
       TaskDTO task = taskFacadeImpl.getTaskByIdInProject(projectId, taskId);
        log.info("[TaskController - getTaskInProject] - return found task to client");
       return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @Override
    public ResponseEntity<List<TaskSummaryDTO>> getTasksByProject(UUID projectId, String sortBy, String sortOrder) {
        log.info("[TaskController - getTasksByProject] - calling taskFacadeImpl");
        List<TaskSummaryDTO> taskDTOList = taskFacadeImpl.getTasksByProject(projectId, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(taskDTOList);
    }

    @Override
    public ResponseEntity<TaskDetailsDTO> updateTask(UpdateTaskDTO updateTaskDTO) {
        return null;
    }

    @Override
    public ResponseEntity<TaskDetailsDTO> updateTaskAssignee(UUID projectId, UUID taskId, UpdateTaskAssigneeDTO updateTaskAssigneeDTO) {
        log.info("[TaskController - updateTaskAssignee] - calling taskFacadeImpl");
        TaskDetailsDTO task = taskFacadeImpl.updateTaskAssignee(projectId, taskId, updateTaskAssigneeDTO);
        log.info("[TaskController - updateTaskAssignee] - return taskDTO to client");
     return ResponseEntity.status(HttpStatus.OK).body(task);
    }
}
