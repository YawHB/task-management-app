package com.todoapp.project;

import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.task_management_app.api.model.ProjectSummaryDTO;
import com.todoapp.project.facade.ProjectSummaryProjection;
import com.todoapp.project.internal.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Slf4j
public class ProjectDTOConverter {

    public Project toEntity(UUID organisationId , ProjectPost projectPost) {
        log.info("[ProjectDTO Converter - toEntity] -Creating a new Project ");
        Project project =  new Project();
        log.info("[ProjectDTO Converter - toEntity] -sets project name on Project object ");
        project.setName(projectPost.getName());
        log.info("[ProjectDTO Converter - toEntity] -sets organisationId on Project object ");
        project.setOrganisationId(organisationId);
        log.info("[ProjectDTO Converter - toEntity] -returns Project object ");
        return project;
    }

    public ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getId());
        projectDTO.setOrganisationId(project.getOrganisationId());
        projectDTO.setName(project.getName());
        OffsetDateTime offsetDateTime = project.getCreatedAt().atOffset(ZoneOffset.UTC);
        projectDTO.setCreatedAt(offsetDateTime);
        return projectDTO;
    }

    public List<ProjectSummaryDTO> toDTO(List<ProjectSummaryProjection> projects) {
        log.info("[ProjectFacadeImpl - ProjectDTOConverter] -looping through projects to convert into projectSummaryDTOs");
        List<ProjectSummaryDTO> projectSummaryDTOS = new ArrayList<>();
        for( ProjectSummaryProjection p : projects) {
        ProjectSummaryDTO projectSummaryDTO = new ProjectSummaryDTO();
        projectSummaryDTO.setProjectId(p.getProjectId());
        projectSummaryDTO.setProjectName(p.getName());
        projectSummaryDTOS.add(projectSummaryDTO);
        }

        return projectSummaryDTOS;
    }
}


