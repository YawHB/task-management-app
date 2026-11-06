package com.todoapp.project.facade;


import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectMemberDTO;
import com.task_management_app.api.model.ProjectPost;
import com.task_management_app.api.model.ProjectSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface ProjectFacade {

    ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) throws OrganisationNotFoundException;

    /**
     * Checks whether a project exists with the specified ID.
     *
     * @param projectId the UUID of the project to check
     * @throws ProjectNotFoundException if no project exists with the specified ID
     */
    void existsById(UUID projectId);

    /**
     * Retrieves all projects within an organisation that a specific user is a member of.
     * Maps the retrieved project data to ProjectSummaryDTO objects.
     *
     * @param organisationId the ID of the organisation
     * @param userId the ID of the user
     * @return a list of project summaries the user is a member of within the organisation
     */
     List<ProjectSummaryDTO> getProjectsForUserInOrganisation(UUID organisationId, UUID userId);

    /**
     * Adds a user as a member of a project within an organisation.
     *
     * @param organisationId the ID of the organisation
     * @param projectMemberDTO the project membership details, including the project name and user's email
     * @param userId the ID of the user to add to the project
     * @return the created project membership
     * @throws ProjectNotFoundException if the project does not exist within the organisation
     * @throws UserAlreadyInProjectException if the user is already a member of the project
     */
     ProjectMemberDTO addProjectUser(UUID organisationId, ProjectMemberDTO projectMemberDTO, UUID userId);

    /**
     * Removes a user from a project.
     *
     * <p>Validates that the user is a member of the specified project before
     * delegating the removal operation to the project repository.</p>
     *
     * @param projectId the ID of the project from which the user will be removed
     * @param userId the ID of the user to remove from the project
     * @throws UserNotMemberOfProjectException if the user is not a member of the project
     */
    void removeProjectUser(UUID projectId, UUID userId);
}


