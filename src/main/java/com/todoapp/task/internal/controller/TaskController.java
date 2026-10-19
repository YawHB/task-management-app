package com.todoapp.task.internal.controller;

import com.task_management_app.api.TaskApi;
import com.task_management_app.api.model.*;
import com.todoapp.exceptions.*;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.task.facade.TaskSummaryProjection;
import com.todoapp.task.internal.service.TaskFacadeImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController // RestController = Controller + ResponseBody
@AllArgsConstructor
public class TaskController implements TaskApi {

    private final TaskFacadeImpl taskFacadeImpl;
    private final ProjectFacade projectFacade;

    @Override
    public ResponseEntity<TaskDTO> addTask(AddTaskDTO addTaskDTO) {
        try{
            log.info("[TaskController - addTask] - calling facade");
        TaskDTO taskDTO = taskFacadeImpl.addTask(addTaskDTO);
            log.info("[TaskController - adTask] - returning created task to client");
        return  ResponseEntity.status(HttpStatus.OK).body(taskDTO);
        } catch (ProjectNotFoundException exception) {
            log.error("addTask,." + exception.getMessage());
            return ResponseEntity.notFound().build();
        } catch (ParentTaskNotFoundException exception) {
            log.error("addTask - "  + exception.getMessage());
            return ResponseEntity.notFound().build();
        }  catch (UserNotFoundException exception){
            log.error("addTask. - " + exception.getMessage());
            return ResponseEntity.notFound().build();
        } catch (UserNotMemberOfProjectException exception) {
            log.error("addTask.. - " + exception.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (InvalidDateRangeException exception) {
            log.error("addTask, - " + exception.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (InvalidStringException exception) {
            log.error(".addTask - " + exception.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (TaskTitleAlreadyExistsException exception) {
            log.error("add task - " + exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    public ResponseEntity<TaskDTO> getTaskByIdInProject(UUID projectId, UUID taskId) {
        try{
        log.info("[TaskController - getTaskInProject] - calling taskFacadeImpl");
       TaskDTO task = taskFacadeImpl.getTaskByIdInProject(projectId, taskId);
        log.info("[TaskController - getTaskInProject] - return found task to client");
       return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (ProjectNotFoundException exception) {
            log.error("get task - " + exception.getMessage());
            return ResponseEntity.notFound().build();
        } catch (TaskNotFoundInProjectException exception) {
            log.error("get task. - " + exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<TaskSummaryDTO>> getTasksByProject(UUID projectId, String sortBy, String sortOrder) {
        try{

        log.info("[TaskController - getTasksByProject] - calling taskFacadeImpl");
        List<TaskSummaryDTO> taskDTOList = taskFacadeImpl.getTasksByProject(projectId, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(taskDTOList);
        } catch (ProjectNotFoundException exception) {
            log.error("get tasks by project - " + exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<TaskDTO> updateTask(UpdateTaskDTO updateTaskDTO) {
        return null;
    }

    @Override
    public ResponseEntity<TaskDetailsDTO> updateTaskAssignee(UUID projectId, UUID taskId, UpdateTaskAssigneeDTO updateTaskAssigneeDTO) {
        try{

        log.info("[TaskController - updateTaskAssignee] - calling taskFacadeImpl");
        TaskDetailsDTO task = taskFacadeImpl.updateTaskAssignee(projectId, taskId, updateTaskAssigneeDTO);
        log.info("[TaskController - updateTaskAssignee] - return taskDTO to client");
     return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (ProjectNotFoundException exception) {
           log.error("TaskController - updateTaskAssignee " + exception.getMessage());
        } catch (TaskNotFoundInProjectException exception) {
           log.error("TaskController - updateTaskAssignee. " + exception.getMessage());
        } catch (UserNotMemberOfProjectException exception) {
           log.error("TaskController - updateTaskAssignee, " + exception.getMessage());
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }




}
