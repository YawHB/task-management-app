package com.todoapp.organisation.internal.service;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserExistsInOrganisationException;
import com.todoapp.organisation.OrganisationDTOConverter;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.organisation.facade.OrganisationDetailsProjection;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.user.UserDTOConverter;
import com.todoapp.user.facade.OrganisationProjection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrganisationFacadeImpl implements OrganisationFacade {

    private final OrganisationRepository organisationRepository;
    private final OrganisationDTOConverter organisationDTOConverter;
    private final UserDTOConverter userDTOConverter; //TODO - only temporarily. refactor to organisation mapStruct


    public OrganisationDTO addOrganisation(AddOrganisationRequestDTO orgRequestDTO) {
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling organisationDTOConverter ");
        Organisation organisation = organisationDTOConverter.toEntity(orgRequestDTO);
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
        Organisation newOrganisation = organisationRepository.save(organisation);
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the toDTO to populate organisationDTO for the controller");
        return organisationDTOConverter.toDTO(newOrganisation);
    }

    @Override
    public OrganisationDetailsDTO findOrganisationById(UUID id) throws OrganisationNotFoundException {
        log.info("[OrganisationFacadeImpl - findOrganisationById] - calling org repo to find org by id");
        Optional<Organisation> organisation = organisationRepository.findById(id);
        if (organisation.isPresent()) {
            log.info("[OrganisationFacadeImpl - findOrganisationById] - calling org repo to get org details(projects + users)");
            List<OrganisationDetailsProjection> organisationDetailsProjection = organisationRepository.findOrganisationWithProjectsAndMembers(id);
            return organisationDTOConverter.toDTO(organisationDetailsProjection);
        }
        throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
    }

    @Override
    public boolean existsById(UUID organisationId) {
        Optional<Organisation> exists = organisationRepository.findById(organisationId);
        return exists.isPresent();
    }

    @Override
    public UserDTO addUserToOrganisation(UUID organisationId, UserDTO userDTO) throws OrganisationNotFoundException {
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling organisation repo to find organisation if exist");
        organisationRepository.findById(organisationId).orElseThrow(
                () -> new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue()));

        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - check if user is already member in org");
        boolean userAlreadyAdded = organisationRepository.userExistInOrganisation(organisationId, userDTO.getUserId());

        if (userAlreadyAdded) {
        throw new UserExistsInOrganisationException(ExceptionReason.USER_ALREADY_EXIST_IN_ORG.getValue());
        }
            log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling organisation repo to add the user to the organisation");
            organisationRepository.addUserToOrganisation(LocalDateTime.now(), UUID.randomUUID(), organisationId, userDTO.getUserId());
            return userDTO;
    }

    @Override
    public boolean userExistInOrganisation(UUID organisationId, UUID userId) {
        return organisationRepository.userExistInOrganisation(organisationId, userId);
    }

    @Override
    public List<OrganisationDTO> getOrganisationsByUserId(UUID userId) {
        log.info("[OrganisationFacadeImpl - getOrganisationsByUserId] - calling repo to fetch organisations for user");
        List<OrganisationProjection> organisations = organisationRepository.findOrganisationsByUserId(userId);
        log.info("[OrganisationFacadeImpl - getOrganisationsByUserId] - convert response to DTO");
       return userDTOConverter.toDTO(organisations);
    }

}




