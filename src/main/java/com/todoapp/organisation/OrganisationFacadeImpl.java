package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
//import com.task_management_app.api.model.Organisation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
 class OrganisationFacadeImpl implements OrganisationFacade {

    private OrganisationRepository organisationRepository;
    private OrganisationDTOConverter organisationDTOConverter;

    @Override
    public Organisation addOrganisation(OrganisationDTO organisationDTO) {
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
          //  return organisationRepository.save((organisationDTO));
        com.todoapp.organisation.Organisation organisation = organisationDTOConverter.toEntity(organisationDTO);
            return organisationRepository.save(organisation);
    }

    @Override
    public Organisation findOrganisationById(UUID id) {
        return null;
    }
}





