package com.todoapp.organisation.facade;

import com.task_management_app.api.model.OrganisationDetailsDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;


import java.util.UUID;

public interface OrganisationFacade  {

    /**
     * Retrieves an organisation by its ID.
     *
     * @param id the UUID of the organisation to retrieve
     * @return the details of the specified organisation
     * @throws OrganisationNotFoundException if the organisation does not exist
     */
    OrganisationDetailsDTO findOrganisationById(UUID id) throws OrganisationNotFoundException;

    /**
     * Checks whether an organisation exists with the specified ID.
     *
     * @param organisationId the UUID of the organisation to check
     * @return {@code true} if the organisation exists, {@code false} otherwise
     */
   boolean existsById(UUID organisationId);

    /**
     * Adds a user to an organisation using the user's email address.
     *
     * @param id the UUID of the organisation to add the user to
     * @param userEmail the email address of the user to add
     * @return the details of the added user
     * @throws OrganisationNotFoundException if the organisation does not exist
     */
    UserDTO addUserToOrganisation(UUID id, String userEmail) throws OrganisationNotFoundException;

    /**
     * Checks whether a user belongs to the specified organisation.
     *
     * @param organisationId the UUID of the organisation to check
     * @param userId the UUID of the user to check
     * @return {@code true} if the user belongs to the organisation, {@code false} otherwise
     */
    boolean userExistInOrganisation(UUID organisationId, UUID userId);
}
