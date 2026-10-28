package com.todoapp.project.facade.mapper;

import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.project.facade.ProjectProjection;
import com.todoapp.project.internal.model.Project;
import com.todoapp.user.facade.UserSummaryProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ProjectMapper {


    Project toEntity(UUID organisationId, ProjectPost projectDTO);


    @Mapping(target = "name", source = "projectName")
    ProjectDTO toDTO(ProjectProjection project);

     @Mapping(target = "userId", source = "id")
     UserDTO toUserDTO(UserSummaryProjection userSummaryProjection);


    //List<ProjectSummaryDTO> toProjectSummaryDTO(ProjectSummaryProjection projects);


    default OffsetDateTime toUtcOffsetDateTimeOrNull(LocalDateTime time) {
        return time != null
                ? time.atOffset(ZoneOffset.UTC)
                : null;
    }
}
