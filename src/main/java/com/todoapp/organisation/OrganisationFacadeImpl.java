package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptions.ExceptionReason;
import com.todoapp.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptions.UserExistsInOrganisationException;
import com.todoapp.users.UserFacade;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
 class OrganisationFacadeImpl implements OrganisationFacade {

    private OrganisationRepository organisationRepository;
    private OrganisationDTOConverter organisationDTOConverter;
    private UserFacade userFacade;


    public Organisation addOrganisation(OrganisationDTO organisationDTO) {
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
        Organisation organisation = organisationDTOConverter.toEntity(organisationDTO);
        return organisationRepository.save(organisation);
    }

    @Override
    public OrganisationDTO findOrganisationById(UUID id) throws OrganisationNotFoundException {
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
    @Transactional
    public UserDTO addUserToOrganisation(UUID organisationId, String userEmail) throws OrganisationNotFoundException {
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling userFacade to find user by email");
        UserDTO userDTO = userFacade.findUserByEmail(userEmail);

        organisationRepository.findById(organisationId).orElseThrow(
                ()-> new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue()));


        //TODO - remember to add invalid email exception as well
        boolean userAlreadyAdded = organisationRepository.userExistInOrganisation(organisationId, userDTO.getUserId());

        if(!userAlreadyAdded) {
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling organisation repo to add the user to the organisation");
        organisationRepository.addUserToOrganisation(LocalDateTime.now(), UUID.randomUUID(), organisationId, userDTO.getUserId());
        return userDTO;
        }
        throw new UserExistsInOrganisationException(ExceptionReason.USER_ALREADY_EXIST_IN_ORG.getValue());


    }
}




