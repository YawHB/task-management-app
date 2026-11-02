package com.todoapp.user.facade;

import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface UserFacade {

    UserDTO addUser(UserDTO userDTO);

    UserDTO getUserById(UUID userId);

    /**
     * Retrieves a user by their email address.
     *
     * @param userEmail (String) the email address of the user
     * @return the details of the found user
     * @throws UserNotFoundException if no user with the given email address exists
     */
    UserDTO getUserByEmail(String userEmail);

    List<UserSummaryDTO> getUsersInProject(UUID projectId);

    List<UserSummaryDTO> findMembersInOrganisation(UUID organisationId);

   boolean existById(UUID id);

   boolean userExistInProject(UUID userId, UUID projectId);

    /**
     * Validates that a user is a member of a specific organisation.
     *
     * @param userId the ID of the user
     * @param organisationId the ID of the organisation
     * @throws UserNotMemberOfOrganisationException if the user is not a member of the organisation
     */
   void userExistInOrganisation(UUID userId, UUID organisationId);

    void deleteTask(UUID id);

}
