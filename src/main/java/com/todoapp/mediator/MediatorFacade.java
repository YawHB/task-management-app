package com.todoapp.mediator;

import com.task_management_app.api.model.AddOrganisationRequestDTO;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
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

}
