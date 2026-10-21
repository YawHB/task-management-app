package com.todoapp.project.internal.controller;


import com.task_management_app.api.ProjectApi;
import com.task_management_app.api.model.ProjectMemberDTO;
import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.task_management_app.api.model.ProjectSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.project.internal.service.ProjectFacadeImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class ProjectController implements ProjectApi {

    ProjectFacadeImpl projectService;

    @Override
    public ResponseEntity<ProjectDTO> addProject(UUID organisationId, ProjectPost ProjectPost) {

            log.info("[ProjectController - addProject] - calling facade");
            ProjectDTO projectDTO = projectService.addProject(organisationId, ProjectPost);
            log.info("[ProjectController - addProject] - Returns response to the client");
            return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
    }

    @Override
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId){
        log.info("[ProjectController - getProjectsForMemberInOrganisation] - calling projectService with org id and user id");
        List<ProjectSummaryDTO> projects = projectService.getProjectsForMemberInOrganisation(organisationId, userId);
        log.info("[ProjectController - getProjectsForMemberInOrganisation] - returning status code OK with the list of projectDTOs");
       return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @Override
    public ResponseEntity<ProjectMemberDTO> addProjectMember( UUID organisationId, ProjectMemberDTO projectMemberDTO) {

        log.info("[ProjectController - addProjectMember] - calling projectService with org id and ");
        ProjectMemberDTO pmDto = projectService.addProjectMember(organisationId, projectMemberDTO);
        log.info("[ProjectController - addProjectMember] - return a 201 status response to client. user added to project ");
        return ResponseEntity.status(HttpStatus.CREATED).body(pmDto);
    }

    @Override
    public ResponseEntity<Void> deleteProject(UUID id) {
        log.info("[ProjectController - deleteProject] - calling projectService");
        projectService.deleteProject(id);
        log.info("[ProjectController - deleteProject] - Project deleted. Returns 204 project deleted✅");
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> removeProjectUser(UUID organisationId, UUID projectId, UUID userId) {

        log.info("[ProjectController - removeProjectUser] - calling project service");
        projectService.removeProjectUser(organisationId, projectId, userId);
        log.info("[ProjectController - removeProjectUser] - return a success noContent(204) ResponseEntity✅");
        return ResponseEntity.noContent().build();
    }
}
