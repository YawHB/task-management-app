package com.todoapp.mediator;

import com.task_management_app.api.model.UserDTO;

import java.util.UUID;

public interface MediatorFacade {
    UserDTO subscribeMemberToOrganisation(UUID organisationId, String userEmail);

}
