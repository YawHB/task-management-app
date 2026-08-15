package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;

import java.util.UUID;

public interface OrganisationFacade  {

    Organisation addOrganisation(OrganisationDTO organisationDTO);

    Organisation findOrganisationById(UUID id);
}
