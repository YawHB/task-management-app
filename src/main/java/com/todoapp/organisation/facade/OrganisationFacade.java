package com.todoapp.organisation.facade;

import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.OrganisationDetailsDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserExistsInOrganisationException;


import java.util.List;
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
     * Adds a user to an organisation using the user's id.
     *
     * @param id the UUID of the organisation to add the user to
     * @param userDTO the user to add to the organisation
     * @return the details of the added user
     * @throws OrganisationNotFoundException if the organisation does not exist
     * @throws UserExistsInOrganisationException if the user is already a member of the organisation
     */
    UserDTO addUserToOrganisation(UUID id, UserDTO userDTO) throws OrganisationNotFoundException, UserExistsInOrganisationException;

    /**
     * Checks whether a user belongs to the specified organisation.
     *
     * @param organisationId the UUID of the organisation to check
     * @param userId the UUID of the user to check
     * @return {@code true} if the user belongs to the organisation, {@code false} otherwise
     */
    boolean userExistInOrganisation(UUID organisationId, UUID userId);

    /**
     * Retrieves all organisations associated with the specified user.
     *
     * @param userId the UUID of the user whose organisations are to be retrieved
     * @return a list of organisations associated with the user
     */
    List<OrganisationDTO> getOrganisationsByUserId(UUID userId);
}
