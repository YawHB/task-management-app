package com.todoapp.mediator;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.ProjectNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
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
    public OrganisationDTO createOrganisation(AddOrganisationRequestDTO orgRequestDTO) {
        /*TODO - delegate the throw responsibility of all facade methods to the facadeImpl through a validater class.
           That way the mediator does not need conditional logic, which helps it remove responsibility
           and stay clean
         */
        userFacade.existById(orgRequestDTO.getCreatorId());
        return organisationFacade.addOrganisation(orgRequestDTO);
    }

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
        projectFacade.existsById(projectId);
        log.info("[mediatorFacadeImpl - getProjectMembers] - calling userFacade to get members in project");
        return userFacade.getUsersInProject(projectId);
    }

    @Override
    public List<OrganisationDTO> getOrganisationsByUserId(UUID userId) {
        log.info("[mediatorFacadeImpl - getOrganisationsByUserId] - check if user id is valid");
        userFacade.existById(userId);
        log.info("[mediatorFacadeImpl - getOrganisationsByUserId] - calling organisationFacade");
        return organisationFacade.getOrganisationsByUserId(userId);
    }

    @Override
    public List<UserSummaryDTO> getUsersInOrganisation(UUID organisationId) {
        organisationFacade.existsById(organisationId);
        return userFacade.findMembersInOrganisation(organisationId);
    }

    @Override
    public ProjectDTO addProjectToOrganisation(UUID organisationId, ProjectPost newProject) {
        organisationFacade.existsById(organisationId);
        return projectFacade.addProject(organisationId, newProject);
    }

    @Override
    public List<ProjectSummaryDTO> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId) {
        organisationFacade.existsById(organisationId);
        organisationFacade.userExistInOrganisation(organisationId, userId);
        return projectFacade.getProjectsForUserInOrganisation(organisationId, userId);
    }

    @Override
    public ProjectMemberDTO addProjectMember(UUID organisationId, ProjectMemberDTO projectMemberDTO) {
        UserDTO foundUser =  userFacade.getUserByEmail(projectMemberDTO.getEmail());
        userFacade.userExistInOrganisation(foundUser.getUserId(), organisationId);
        return projectFacade.addProjectUser(organisationId, projectMemberDTO, foundUser.getUserId() );
    }

    @Override
    public void removeProjectMember(UUID organisationId, UUID projectId, UUID userId) {
        organisationFacade.existsById(organisationId);
        projectFacade.existsById(projectId);
        userFacade.existById(userId);
        organisationFacade.userExistInOrganisation(organisationId, userId);
        projectFacade.removeProjectUser(projectId, userId);
    }


}
