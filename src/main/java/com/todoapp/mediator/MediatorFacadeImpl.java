package com.todoapp.mediator;

import com.task_management_app.api.model.UserDTO;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.user.facade.UserFacade;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j

@Component
@AllArgsConstructor
public class MediatorFacadeImpl implements MediatorFacade {
    private UserFacade userFacade;
    private OrganisationFacade organisationFacade;


    @Override
    @Transactional
    public UserDTO subscribeMemberToOrganisation(UUID organisationId, String memberEmail) {
        log.info("[mediatorFacadeImpl - subscribeMemberToOrganisation] - calling userFacade to find user by email");
        UserDTO userDTO = userFacade.getUserByEmail(memberEmail);
        log.info("[mediatorFacadeImpl - subscribeMemberToOrganisation] - calling organisationFacade to to add member to organisation");
       return organisationFacade.addUserToOrganisation( organisationId, userDTO);

    }
}
