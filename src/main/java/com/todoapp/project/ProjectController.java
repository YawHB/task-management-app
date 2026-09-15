package com.todoapp.project;


import com.task_management_app.api.ProjectApi;
import com.task_management_app.api.model.ProjectMemberDTO;
import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptions.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class ProjectController implements ProjectApi {

    private final ProjectFacade projectFacade;
    private final ProjectFacadeImpl projectService;

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
}
