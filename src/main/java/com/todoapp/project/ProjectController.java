package com.todoapp.project;


import com.task_management_app.api.ProjectApi;
import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptions.ExceptionReason;
import com.todoapp.exceptions.OrganisationNotFoundException;
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

    ProjectFacade projectFacade;

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
}


