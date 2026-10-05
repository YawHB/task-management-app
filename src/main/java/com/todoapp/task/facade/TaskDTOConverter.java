package com.todoapp.task.facade;
import com.task_management_app.api.model.AddTaskDTO;
import com.task_management_app.api.model.PriorityDTO;
import com.task_management_app.api.model.StatusDTO;
import com.task_management_app.api.model.TaskDTO;
import com.todoapp.task.internal.Priority;
import com.todoapp.task.internal.Status;
import com.todoapp.task.internal.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.ZoneOffset;

@Slf4j
@Component
public class TaskDTOConverter {
    public Task toEntity(AddTaskDTO requestBody) {

        log.info("[TaskDTOConverer - toEntity] - mapping DTO -> entity");
        Task newTask = new Task();

        newTask.setTitle(requestBody.getTaskTitle());
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

        taskDTO.setCreatedAt(task.getCreatedAt().atOffset(ZoneOffset.UTC));
        taskDTO.setUpdatedAt(task.getUpdatedAt().atOffset(ZoneOffset.UTC));
        return taskDTO;
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
}
