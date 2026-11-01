package com.todoapp.organisation.internal.service;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.organisation.facade.mapper.OrganisationMapper;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.organisation.facade.OrganisationDetailsProjection;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.organisation.internal.model.Organisation;
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
    private final OrganisationMapper organisationMapper;


    //TODO extract existsByName to validator - return void or throw
    @Override
    public OrganisationDTO addOrganisation(AddOrganisationRequestDTO orgRequestDTO) {
        String name = orgRequestDTO.getOrganisationName();
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling organisationMapper ");
        if(organisationRepository.existsByName(name)) throw new DuplicateOrganisationNameException(ExceptionReason.DUPLICATE_ORG_NAME.getValue());
        Organisation organisation = organisationMapper.toEntity(orgRequestDTO);
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
        Organisation newOrganisation = organisationRepository.save(organisation);
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the toDTO to populate organisationDTO for the controller");
        return organisationMapper.toDTO(newOrganisation);
    }

    //TODO extract findById to validator - return organisation or throw
    @Override
    public OrganisationDetailsDTO findOrganisationById(UUID id) throws OrganisationNotFoundException {
        log.info("[OrganisationFacadeImpl - findOrganisationById] - calling org repo to find org by id");
        Optional<Organisation> organisation = organisationRepository.findById(id);
        if (organisation.isPresent()) {
            log.info("[OrganisationFacadeImpl - findOrganisationById] - calling org repo to get org details(projects + users)");
            List<OrganisationDetailsProjection> organisationDetailsProjection = organisationRepository.findOrganisationWithProjectsAndMembers(id);
            return organisationMapper.toDTO(organisationDetailsProjection);
        }
        throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
    }

    //TODO extract findById to validator - return organisation or throw
    @Override
    public boolean existsById(UUID organisationId) {
        log.info("[OrganisationFacadeImpl - existsById] - calling organisation repository to find org by id");
        Optional<Organisation> exists = organisationRepository.findById(organisationId);
        if(exists.isEmpty()) {
        throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
        }
        return true;
    }

    //TODO extract findById to validator - return organisation or throw
    @Override
    public UserDTO addUserToOrganisation(UUID organisationId, UserDTO userDTO) throws OrganisationNotFoundException {
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling organisation repo to find organisation if exist");
        organisationRepository.findById(organisationId).orElseThrow(
                () -> new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue()));

        //TODO extract userExistInOrganisation to validator - return organisation or throw
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - check if user is already member in org");
        boolean userAlreadyAdded = organisationRepository.userExistInOrganisation(organisationId, userDTO.getUserId());
        if (userAlreadyAdded) {
        throw new UserExistsInOrganisationException(ExceptionReason.USER_ALREADY_EXIST_IN_ORG.getValue());
        }
            log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling organisation repo to add the user to the organisation");
            organisationRepository.addUserToOrganisation(LocalDateTime.now(), UUID.randomUUID(), organisationId, userDTO.getUserId());
            return userDTO;
    }

    //TODO extract userExistInOrganisation to validator - return organisation or throw
    @Override
    public boolean userExistInOrganisation(UUID organisationId, UUID userId) {
        return organisationRepository.userExistInOrganisation(organisationId, userId);
    }

    @Override
    public List<OrganisationDTO> getOrganisationsByUserId(UUID userId) {
        log.info("[OrganisationFacadeImpl - getOrganisationsByUserId] - calling repo to fetch organisations for user");
        List<OrganisationProjection> organisations = organisationRepository.findOrganisationsByUserId(userId);
        log.info("[OrganisationFacadeImpl - getOrganisationsByUserId] - convert response to DTO");
       return organisationMapper.toOrganisationDTO(organisations);
    }

}




