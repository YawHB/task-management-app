package com.todoapp.organisation.internal.validation;

import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserExistsInOrganisationException;
import com.todoapp.exceptionHandling.exceptions.UserNotMemberOfOrganisationException;

import com.todoapp.organisation.internal.repository.OrganisationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class OrganisationValidationHandler {

    private final OrganisationRepository organisationRepository;


    /**
     * Validates that a user is a member of an organisation.
     * Throws a UserNotMemberOfOrganisationException if the user is not a member.
     *
     * @param organisationId the ID of the organisation
     * @param userId the ID of the user
     */
    public void validateUserExistsInOrganisation(UUID organisationId, UUID userId) {
      if(!organisationRepository.userExistInOrganisation(organisationId, userId))
          throw new UserNotMemberOfOrganisationException(ExceptionReason.USER_NOT_MEMBER_OF_ORG.getValue());
    }

    /**
     * Validates that a user is not already a member of an organisation.
     * Throws a UserExistsInOrganisationException if the user is already a member.
     *
     * @param organisationId the ID of the organisation
     * @param userId the ID of the user
     */
    public void validateUserIsNotMemberOfOrganisation(UUID organisationId, UUID userId){
        if(organisationRepository.userExistInOrganisation(organisationId, userId))
            throw new UserExistsInOrganisationException( ExceptionReason.USER_ALREADY_EXIST_IN_ORG.getValue());
    }

    /**
     * Validates that an organisation exists with the specified ID.
     *
     * @param organisationId the ID of the organisation to validate
     * @throws OrganisationNotFoundException if no organisation exists with the specified ID
     */
    public void validateExistsById(UUID organisationId) {
        log.info("[OrganisationFacadeImpl - existsById] - calling organisation repository to find org by id");
        if(!organisationRepository.existsById(organisationId)){
            throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
        }
    }
}
