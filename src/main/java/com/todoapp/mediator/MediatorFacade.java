package com.todoapp.mediator;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface MediatorFacade {
    /**
     * Subscribes an existing user to an organisation using their email address.
     *
     * @param organisationId the UUID of the organisation to subscribe the user to
     * @return the details of the subscribed user
     * @throws UserNotFoundException if no user with the given email address exists
     * @throws OrganisationNotFoundException if the organisation does not exist
     * @throws UserExistsInOrganisationException if the user is already a member
     */
    UserDTO subscribeMemberToOrganisation(UUID organisationId, String userEmail);
    List<UserSummaryDTO> getProjectMembers(UUID projectId);
    List<OrganisationDTO> getOrganisationsByUserId(UUID userId);
    List<UserSummaryDTO> getUsersInOrganisation(UUID organisationId);
    OrganisationDTO createOrganisation(AddOrganisationRequestDTO orgRequestDTO);
    ProjectDTO addProjectToOrganisation(UUID organisationId, ProjectPost newProject);
    /**
     * Retrieves all projects in an organisation that a specific user is a member of.
     * Validates that the organisation exists and that the user is a member of the organisation
     * before retrieving the projects.
     *
     * @param organisationId the ID of the organisation
     * @param userId the ID of the user
     * @return a list of projects the user is a member of within the organisation
     * @throws OrganisationNotFoundException if the organisation does not exist
     * @throws UserNotMemberOfOrganisationException if the user is not a member of the organisation
     */
    List<ProjectSummaryDTO> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId);

}
