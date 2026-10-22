package com.todoapp.task.internal.mapper;

import com.task_management_app.api.model.AddTaskDTO;
import com.task_management_app.api.model.TaskDTO;
import com.task_management_app.api.model.TaskSummaryDTO;
import com.todoapp.task.facade.TaskSummaryProjection;
import com.todoapp.task.internal.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "taskTitle", source = "title")
    TaskDTO toDTO(Task task);


   @Mapping(target = "id",  ignore = true)
   @Mapping(target = "title", source = "taskTitle")
   Task toEntity(AddTaskDTO addTaskDTO);


    default OffsetDateTime toUtcOffsetDateTimeOrNull(LocalDateTime time) {
        return time != null
                ? time.atOffset(ZoneOffset.UTC)
                : null;
    }

    @Mapping(target = "taskId", source = "id")
    TaskSummaryDTO toTaskSummaryDTO(TaskSummaryProjection task);

    List<TaskSummaryDTO> toTaskSummaryDTO(List<TaskSummaryProjection> tasks);


    default LocalDateTime toLocalDateTimeOrNull(OffsetDateTime time) {
        return time != null
                ? time.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime()
                : null;
    }

}