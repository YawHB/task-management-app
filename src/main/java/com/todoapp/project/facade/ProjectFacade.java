package com.todoapp.project.facade;


import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptions.OrganisationNotFoundException;

import java.util.UUID;

public interface ProjectFacade {

    ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) throws OrganisationNotFoundException;

    /**

     Checks whether a project exists with the specified ID.
     @param projectId the UUID of the project to check
     @return {@code true} if the project exists, {@code false} otherwise
     */
    boolean existsById(UUID projectId);





    }


