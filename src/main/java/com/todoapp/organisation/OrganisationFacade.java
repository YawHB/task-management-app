package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.OrganisationDetailsDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptions.OrganisationNotFoundException;


import java.util.Optional;
import java.util.UUID;

public interface OrganisationFacade  {

    OrganisationDetailsDTO findOrganisationById(UUID id) throws OrganisationNotFoundException;

   boolean existsById(UUID organisationId);


    UserDTO addUserToOrganisation(UUID id, String userEmail) throws OrganisationNotFoundException;
}
