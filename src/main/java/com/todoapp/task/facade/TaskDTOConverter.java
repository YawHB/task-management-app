package com.todoapp.task.facade;
import com.task_management_app.api.model.*;
import com.todoapp.exceptions.ExceptionReason;
import com.todoapp.exceptions.InvalidStringException;
import com.todoapp.task.internal.Priority;
import com.todoapp.task.internal.Status;
import com.todoapp.task.internal.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TaskDTOConverter {
    public Task toEntity(AddTaskDTO requestBody) {

        log.info("[TaskDTOConverer - toEntity] - mapping DTO -> entity");
        Task newTask = new Task();

        newTask.setTitle(notBlank(requestBody.getTaskTitle()));
        newTask.setDescription(requestBody.getDescription());
        newTask.setParentTaskId(requestBody.getParentTaskId());
        newTask.setProjectId(requestBody.getProjectId());
        newTask.setAssigneeId(requestBody.getAssigneeId());
        newTask.setTaskCreatorId(requestBody.getTaskCreatorId());
       newTask.setStatus(requestBody.getStatus() != null ?
                mapStatusToEntity(requestBody.getStatus()) :
               Status.TODO);
        newTask.setPriority(requestBody.getPriority() != null ?
                mapPriority(requestBody.getPriority()) :
                Priority.MEDIUM);
        newTask.setDueDate(requestBody.getDueDate());

        return newTask;
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setTaskId(task.getId());
        taskDTO.setTaskTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setParentTaskId(task.getParentTaskId());
        taskDTO.setProjectId(task.getProjectId());
        taskDTO.setAssigneeId(task.getAssigneeId());
        taskDTO.setTaskCreatorId(task.getTaskCreatorId());
        taskDTO.setStatus(mapStatusToDTO(task.getStatus()));
        taskDTO.setPriority(mapPriorityToDTO(task.getPriority()));
        taskDTO.setDueDate(task.getDueDate());

        taskDTO.setCreatedAt(applyUtcOffset(task.getCreatedAt()));
        taskDTO.setUpdatedAt(applyUtcOffset(task.getCreatedAt()));
        return taskDTO;
    }

    public List<TaskSummaryDTO> toDTO(List<TaskSummaryProjection> tasks) {
        log.info("[TaskDTOConverer - toDTO] - mapping list of Tasks --> list of TaskDTOs");

        List<TaskSummaryDTO> taskDTOS = new ArrayList<>();

        for(TaskSummaryProjection task : tasks) {
            TaskSummaryDTO taskDTO = new TaskSummaryDTO();

        taskDTO.setTaskId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setProjectId(task.getProjectId());
        taskDTO.setFirstName(task.getFirstName());
        taskDTO.setLastName(task.getLastName());
        taskDTO.setStatus(mapStatusToDTO(task.getStatus()));
        taskDTO.setPriority(mapPriorityToDTO(task.getPriority()));
        taskDTO.setCreatedAt(task.getCreatedAt().atOffset(ZoneOffset.UTC));
        taskDTO.setCreatedAt(applyUtcOffset(task.getCreatedAt()));
        taskDTO.setDueDate(task.getDueDate());

        taskDTOS.add(taskDTO);
        }
        return taskDTOS;
    }

    private OffsetDateTime applyUtcOffset(LocalDateTime time) {
        return time.atOffset(ZoneOffset.UTC);
    }

    private Status mapStatusToEntity(StatusDTO status) {
        return Status.valueOf(status.name());
    }
    private StatusDTO mapStatusToDTO(Status status) {
        return StatusDTO.valueOf(status.name());

    }
    private Priority mapPriority(PriorityDTO priority) {
        return Priority.valueOf(priority.name());
    }
    private PriorityDTO mapPriorityToDTO(Priority priority) {
        return PriorityDTO.valueOf(priority.name());
    }

    private String notBlank(String string) {
        log.info("[TaskDTOConverer - notBlank] - checking if string is blank");

        if(string.isBlank()){
        log.info("[TaskDTOConverer - notBlank] - STRING IS BLANK");
            throw new InvalidStringException(ExceptionReason.INVALID_STRING.getValue());
        }

        return string;
    }
}


