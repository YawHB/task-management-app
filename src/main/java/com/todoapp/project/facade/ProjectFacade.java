package com.todoapp.project.facade;


import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.task_management_app.api.model.ProjectSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ProjectFacade {

    ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) throws OrganisationNotFoundException;

    /**

     Checks whether a project exists with the specified ID.
     @param projectId the UUID of the project to check
     @return {@code true} if the project exists, {@code false} otherwise
     */
    boolean existsById(UUID projectId);

    /**
     * Retrieves all projects within an organisation that a specific user is a member of.
     * Maps the retrieved project data to ProjectSummaryDTO objects.
     *
     * @param organisationId the ID of the organisation
     * @param userId the ID of the user
     * @return a list of project summaries the user is a member of within the organisation
     */
     List<ProjectSummaryDTO> getProjectsForUserInOrganisation(UUID organisationId, UUID userId);






    }


