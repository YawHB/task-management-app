package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptions.ExceptionReason;
import com.todoapp.exceptions.OrganisationNotFoundException;
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

    @Override
    public Organisation addOrganisation(OrganisationDTO organisationDTO) {
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
        Organisation organisation = organisationDTOConverter.toEntity(organisationDTO);
        return organisationRepository.save(organisation);
    }

    @Override
    public OrganisationDTO findOrganisationById(UUID id) throws OrganisationNotFoundException {
        Optional<Organisation> organisation = organisationRepository.findById(id);
        if (organisation.isPresent()) {
            return organisationDTOConverter.toDTO(organisation.get());
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
    public UserDTO addUserToOrganisation(UUID organisationId, String userEmail) {
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling userFacade to find user by email");
        UserDTO userDTO = userFacade.findUserByEmail(userEmail);

        //boolean userAlreadyAdded = organisationRepository.userExistInOrganisation(organisationId, userDTO.getUserId()); TODO check if user already exist in org.
        //if(!userAlreadyAdded) {
        log.info("[OrganisationFacadeImpl - addUserToOrganisation] - calling organisation repo to add the user to the organisation");
        organisationRepository.addUserToOrganisation(LocalDateTime.now(), UUID.randomUUID(), organisationId, userDTO.getUserId());
        return userDTO;
       // }
    }
}




