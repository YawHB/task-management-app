package com.todoapp.user.internal.validator;

import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserNotMemberOfOrganisationException;
import com.todoapp.user.internal.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserValidationHandler {

    private final UserRepository userRepository;


    /**
     * Validates that a user is a member of a specific organisation.
     *
     * @param userId the ID of the user
     * @param organisationId the ID of the organisation
     * @throws UserNotMemberOfOrganisationException if the user is not a member of the organisation
     */
    public void validateUserExistsInOrganisation(UUID userId, UUID organisationId){
        if(!userRepository.userMemberOfOrganisation(userId, organisationId)){
            throw new UserNotMemberOfOrganisationException(ExceptionReason.USER_NOT_MEMBER_OF_ORG.getValue());
        }
    }

    /**
     * Validates that a user exists with the specified ID.
     *
     * @param userId the UUID of the user to validate
     * @throws UserNotFoundException if no user exists with the specified ID
     */
    public void validateExistsById(UUID userId) {
       if(! userRepository.existsById(userId)){
           throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue());
       }
    }
}
