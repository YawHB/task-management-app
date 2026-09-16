package com.todoapp.project;


import com.task_management_app.api.ProjectApi;
import com.task_management_app.api.model.ProjectMemberDTO;
import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptions.*;
import com.todoapp.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptions.UserNotMemberOfOrganisationException;
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

    ProjectFacade projectFacade;
    ProjectFacadeImpl projectService;

    @Override
    public ResponseEntity<ProjectDTO> addProject(UUID organisationId, ProjectPost ProjectPost) {

        try{
            log.info("[ProjectController - addProject] - calling facade");
            ProjectDTO projectDTO = projectFacade.addProject(organisationId, ProjectPost);
            log.info("[ProjectController - addProject] - Returns response to the client");
            return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
        } catch (OrganisationNotFoundException exception) {
            log.error("[ProjectController - addProject] - " + exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }



    @Override
    public ResponseEntity<List<ProjectDTO>> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId) throws OrganisationNotFoundException {
        log.info("[ProjectController - getProjectsForMemberInOrganisation] - calling projectService with org id and user id");
        try{
        List<ProjectDTO> projects = projectService.getProjectsForMemberInOrganisation(organisationId, userId);
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
        }
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
        }
        return ResponseEntity.notFound().build();
    }
}
