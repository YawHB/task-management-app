package com.todoapp.mediator;

import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.ProjectNotFoundException;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.user.facade.UserFacade;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Slf4j

@Component
@AllArgsConstructor
public class MediatorFacadeImpl implements MediatorFacade {
    private final UserFacade userFacade;
    private final OrganisationFacade organisationFacade;
    private final ProjectFacade projectFacade;


    @Override
    @Transactional
    public UserDTO subscribeMemberToOrganisation(UUID organisationId, String memberEmail) {
        log.info("[mediatorFacadeImpl - subscribeMemberToOrganisation] - calling userFacade to find user by email");
        UserDTO userDTO = userFacade.getUserByEmail(memberEmail);
        log.info("[mediatorFacadeImpl - subscribeMemberToOrganisation] - calling organisationFacade to to add member to organisation");
       return organisationFacade.addUserToOrganisation( organisationId, userDTO);

    }

    @Override
    public List<UserSummaryDTO> getProjectMembers(UUID projectId) {
        log.info("[mediatorFacadeImpl - getProjectMembers] - calling projectFacade, to see if project exist");
        if(!projectFacade.existsById(projectId))
            throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND.getValue());
        log.info("[mediatorFacadeImpl - getProjectMembers] - calling userFacade to get members in project");
        return userFacade.getUsersInProject(projectId);
    }
}
