package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
//import com.task_management_app.api.model.Organisation;


import java.util.UUID;

public interface OrganisationFacade  {

    Organisation addOrganisation(OrganisationDTO organisationDTO);

    Organisation findOrganisationById(UUID id);

}
