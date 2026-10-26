package com.todoapp.mediator;

import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;

import java.util.List;
import java.util.UUID;

public interface MediatorFacade {
    UserDTO subscribeMemberToOrganisation(UUID organisationId, String userEmail);
    List<UserSummaryDTO> getProjectMembers(UUID projectId);

}
