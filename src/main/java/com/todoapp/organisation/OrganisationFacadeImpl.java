package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.utils.OrganisationDTOConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrganisationFacadeImpl implements OrganisationFacade {

    private OrganisationRepository organisationRepository;
    private OrganisationDTOConverter organisationDTOConverter;

    @Override
    public Organisation addOrganisation(OrganisationDTO organisationDTO) {
       return organisationRepository.save(organisationDTOConverter.toEntity(organisationDTO));
    }

    @Override
    public Organisation findOrganisationById(UUID id) {
        return null;
    }
}
