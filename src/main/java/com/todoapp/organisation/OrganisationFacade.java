package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.exceptions.OrganisationNotFoundException;
//import com.task_management_app.api.model.Organisation;


import java.util.Optional;
import java.util.UUID;

public interface OrganisationFacade  {

    Organisation addOrganisation(OrganisationDTO organisationDTO);

    OrganisationDTO findOrganisationById(UUID id) throws OrganisationNotFoundException;

   boolean existsById(UUID organisationId);


}
