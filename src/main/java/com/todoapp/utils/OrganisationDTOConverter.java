package com.todoapp.utils;

import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.organisation.Organisation;
import org.springframework.stereotype.Component;

@Component
public class OrganisationDTOConverter {

    public Organisation toEntity(OrganisationDTO organisationDTO) {
        Organisation organisation = new Organisation();
        organisation.setName(organisationDTO.getName());
        return organisation;

    }

    public OrganisationDTO toDTO(Organisation organisation) {
        OrganisationDTO organisationDTO = new OrganisationDTO();
        organisationDTO.setName(organisation.getName());
        return organisationDTO;
    }
}
