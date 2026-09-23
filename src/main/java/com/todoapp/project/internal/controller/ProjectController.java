package com.todoapp.project.internal.controller;


import com.task_management_app.api.ProjectApi;
import com.task_management_app.api.model.ProjectMemberDTO;
import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.task_management_app.api.model.ProjectSummaryDTO;
import com.todoapp.exceptions.*;
import com.todoapp.exceptions.UserNotMemberOfOrganisationException;
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

        try {
            log.info("[ProjectController - addProject] - calling facade");
            ProjectDTO projectDTO = projectService.addProject(organisationId, ProjectPost);
            log.info("[ProjectController - addProject] - Returns response to the client");
            return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
        } catch (OrganisationNotFoundException exception) {
            log.error("[ProjectController - addProject] - " + exception.getMessage());
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestBodyException exception) {
            log.error("[ProjectController - addProjectMember] - invalid request body"
                    + exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DuplicateProjectNameException exception) {
            log.error("[ProjectController - addProjectMember] -duplicate project name"
                    + exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @Override
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId){
        try{
        log.info("[ProjectController - getProjectsForMemberInOrganisation] - calling projectService with org id and user id");
        List<ProjectSummaryDTO> projects = projectService.getProjectsForMemberInOrganisation(organisationId, userId);
        log.info("[ProjectController - getProjectsForMemberInOrganisation] - returning status code OK with the list of projectDTOs");
       return ResponseEntity.status(HttpStatus.OK).body(projects);

        } catch (OrganisationNotFoundException exception) {
          log.error("[ProjectController - getProjectsForMemberInOrganisation❌] - " + exception.getMessage());
        } catch (UserNotMemberOfOrganisationException exception) {
            log.error("[ProjectController - getProjectsForMemberInOrganisation❌.] - " + exception.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<ProjectMemberDTO> addProjectMember( UUID organisationId, ProjectMemberDTO projectMemberDTO) {
        try{
            log.info("[ProjectController - addProjectMember] - calling projectService with org id and ");
        ProjectMemberDTO pmDto = projectService.addProjectMember(organisationId, projectMemberDTO);
        log.info("[ProjectController - addProjectMember] - return a 201 status response to client. user added to project ");
        return ResponseEntity.status(HttpStatus.CREATED).body(pmDto);
        } catch (UserAlreadyInProjectException exception) {
            log.error("[ProjectController - addProjectMember] - returns a 409 status response. conflict "
                    + exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }  catch (UserProjectConflictException exception) {
            log.error("[ProjectController - addProjectMember] - returns a 404 status response "
                    + exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotMemberOfOrganisationException exception) {
            log.error("[ProjectController - addProjectMember]. " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @Override
    public ResponseEntity<Void> deleteProject(UUID id) {
        try{
        projectService.deleteProject(id);
            log.info("[ProjectController - deleteProject] - calling projectService");
        } catch (ProjectNotFoundException exception) {
            log.error("[ProjectController - deleteProject] - Returns a 404 project not found❌");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("[ProjectController - deleteProject] - Project deleted. Returns 204 project deleted✅");
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> removeProjectUser(UUID organisationId, UUID projectId, UUID userId) {
        try{
        log.info("[ProjectController - removeProjectUser] - calling project service");
        projectService.removeProjectUser(organisationId, projectId, userId);
        log.info("[ProjectController - removeProjectUser] - return a success noContent(204) ResponseEntity✅");
        return ResponseEntity.noContent().build();
        }catch (OrganisationNotFoundException exception) {
            log.error("[ProjectController - removeProjectUser] - return 404 status response for organisation ❌"+ exception.getMessage());

        } catch (ProjectNotFoundException exception) {
            log.error("[ProjectController - removeProjectUser] - return 404 status response for project ❌"+ exception.getMessage());

        } catch (UserNotFoundException exception) {
            log.error("[ProjectController - removeProjectUser] - return 404 status response for user ❌"+ exception.getMessage());

        } catch (UserNotMemberOfOrganisationException exception) {
            log.error("[ProjectController - removeProjectUser] - user not member of organisation ❌"+ exception.getMessage());

        } catch (UserNotMemberOfProjectException exception) {
            log.error("[ProjectController - removeProjectUser] - user not member of project ❌"+ exception.getMessage());

        }
        return ResponseEntity.notFound().build();
    }
}
