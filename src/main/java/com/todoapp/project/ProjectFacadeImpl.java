package com.todoapp.project;

import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptions.ExceptionReason;
import com.todoapp.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptions.UserNotMemberOfOrganisationException;
import com.todoapp.organisation.OrganisationFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class ProjectFacadeImpl implements ProjectFacade{

    ProjectDTOConverter projectDTOConverter;
    ProjectRepository projectRepository;
    OrganisationFacade organisationFacade;
    @Override
    public ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) throws OrganisationNotFoundException {

        log.info("[ProjectFacadeImpl - addProject] - checks if organisation exists by its id (OBS: missing error handling on fail)");
      if(organisationFacade.existsById(organisationId)) {
          log.info("[ProjectFacadeImpl - addProject] - returns a project from the project DTO converter ");
          Project project = projectDTOConverter.toEntity(organisationId, projectPost);
          log.info("[ProjectFacadeImpl - addProject] - saves Project object ");
          Project createdProject = projectRepository.save(project);
          log.info("[ProjectFacadeImpl - addProject] - returns Project object");
          return projectDTOConverter.toDTO(createdProject);
      }

          System.out.println("ADD EXCEPTION HERE"); //TODO - ADD EXCEPTION HERE
        throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
    }

    public List<ProjectDTO> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId) throws OrganisationNotFoundException {

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling OrganisationFacade to check if organisation exist");
        if(!organisationFacade.existsById(organisationId)) {
        log.error("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - organisation DOES NOT exist❌");
            throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
        } else {
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - organisation exist✅");
        }
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling projectRepository to check if user exist in the organisation");
        boolean userExist = projectRepository.userExistInOrganisation(organisationId, userId);
        if(!userExist) {
        log.error("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - user was not found in organisation❌ - throwing USER_NOT_MEMBER_OF_ORG exception");
        throw new UserNotMemberOfOrganisationException(ExceptionReason.USER_NOT_MEMBER_OF_ORG.getValue());
        }
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - User exist in organisation✅");
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling projectRepository with org id and user id to find all projects in org for user");
        List<ProjectSummaryProjection> projects = projectRepository.findProjectsForMemberInOrganisation(organisationId, userId); //PREPLY - Do we need to store the returned value and handle something?
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - project list returned. Creating empty projectDTOs list");
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] -looping through projects to convert into projectDTOs");
            for (ProjectSummaryProjection p : projects) {
                projectDTOs.add(projectDTOConverter.toDTOFromList(p));
            }

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] -return projectDTOs list to controller");
        return projectDTOs;

    }
}


