package com.todoapp.task.internal.controller;

import com.task_management_app.api.TaskApi;
import com.task_management_app.api.model.*;
import com.todoapp.exceptions.*;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.task.internal.service.TaskFacadeImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public ResponseEntity<TaskDTO> getTaskByInProject(UUID projectId, UUID taskId) {
        try{

        log.info("[TaskController - getTaskInProject] - calling taskFacadeImpl");
       TaskDTO task = taskFacadeImpl.getTaskByInProject(projectId, taskId);
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
    public ResponseEntity<List<TaskDTO>> getTasksByProject(UUID projectId, String sortBy, String sortOrder) {
      return null;
    }


    @Override
    public ResponseEntity<TaskDTO> updateTask(UpdateTaskDTO updateTaskDTO) {
        return null;
    }


}
