package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import org.springframework.stereotype.Component;

@Component
 class OrganisationDTOConverter {

    public Organisation toEntity(OrganisationDTO organisationDTO) {
        Organisation organisation = new Organisation();
        organisation.setName(organisationDTO.getName());

        return organisation;
    }

    public OrganisationDTO toDTO(Organisation organisation) {
        OrganisationDTO organisationDTO = new OrganisationDTO();
        organisationDTO.setOrganisationId(organisation.getId());
        organisationDTO.setName(organisation.getName());

        return organisationDTO;
    }
}
