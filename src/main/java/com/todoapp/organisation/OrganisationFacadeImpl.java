package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.utils.OrganisationDTOConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrganisationFacadeImpl implements OrganisationFacade {

    private OrganisationRepository organisationRepository;
    private OrganisationDTOConverter organisationDTOConverter;

    @Override
    public Organisation addOrganisation(OrganisationDTO organisationDTO) {
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
       return organisationRepository.save(organisationDTOConverter.toEntity(organisationDTO));
    }

    @Override
    public Organisation findOrganisationById(UUID id) {
        return null;
    }
}
