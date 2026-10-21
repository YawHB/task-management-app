package com.todoapp.project.internal.service;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.project.ProjectDTOConverter;
import com.todoapp.project.internal.repository.ProjectRepository;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.project.facade.ProjectSummaryProjection;
import com.todoapp.project.internal.model.Project;
import com.todoapp.user.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class ProjectFacadeImpl implements ProjectFacade {

    private final ProjectDTOConverter projectDTOConverter;
    private final ProjectRepository projectRepository;

    private final OrganisationFacade organisationFacade;
    private final UserFacade userFacade;


    @Override
    public ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) throws OrganisationNotFoundException {
        log.info("[ProjectFacadeImpl - addProject] - validates that request body contains project name)");
        if(projectPost.getName().isBlank()) throw new InvalidRequestBodyException(ExceptionReason.INVALID_REQUEST_BODY.getValue());
        if(projectRepository.existsByName(projectPost.getName()))  throw new DuplicateProjectNameException(ExceptionReason.INVALID_REQUEST_BODY.getValue());
        log.info("[ProjectFacadeImpl - addProject] - checks if organisation exists by its id (OBS: missing error handling on fail)");
        if(organisationFacade.existsById(organisationId)) {
          log.info("[ProjectFacadeImpl - addProject] - returns a project from the project DTO converter ");
          Project project = projectDTOConverter.toEntity(organisationId, projectPost);
          log.info("[ProjectFacadeImpl - addProject] - saves Project object ");
          Project createdProject = projectRepository.save(project);
          log.info("[ProjectFacadeImpl - addProject] - returns Project object");
          return projectDTOConverter.toDTO(createdProject);
      }

        throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
    }

    public List<ProjectSummaryDTO> getProjectsForMemberInOrganisation(UUID organisationId, UUID userId) throws OrganisationNotFoundException {

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling OrganisationFacade to check if organisation exist");
        if(!organisationFacade.existsById(organisationId)) throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling projectRepository to check if user exist in the organisation");
        boolean isMember = organisationFacade.userExistInOrganisation(organisationId, userId);

        if(!isMember) throw new UserNotMemberOfOrganisationException(ExceptionReason.USER_NOT_MEMBER_OF_ORG.getValue());

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling projectRepository with org id and user id to find all projects in org for user");
        List<ProjectSummaryProjection> projects = projectRepository.findProjectsForMemberInOrganisation(organisationId, userId);
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling toDTO");
        List<ProjectSummaryDTO> projectSummaryDTOS = projectDTOConverter.toDTO(projects);
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] -return a list of projectSummaryDTOs to controller");
        return projectSummaryDTOS;
    }

    @Transactional
    public ProjectMemberDTO addProjectMember(UUID organisationId, ProjectMemberDTO projectMemberDTO) {
        log.info("[projectFacadeImpl - addProjectMember] - calling userFacade to get user by email");

       try{
           UserDTO foundUser =  userFacade.getUserByEmail(projectMemberDTO.getEmail());
        log.info("[projectFacadeImpl - addProjectMember] - calling userFacade to check if user exist in org");
        if(!userFacade.userExistInOrganisation(foundUser.getUserId(), organisationId))
            throw new UserNotMemberOfOrganisationException(ExceptionReason.USER_NOT_MEMBER_OF_ORG.getValue());

        log.info("[projectFacadeImpl - addProjectMember] - calling private method getProjectByOrganisationIdAndName");
       ProjectDTO foundProject = getProjectByOrganisationIdAndName( organisationId, projectMemberDTO.getProjectName());
           log.info("[projectFacadeImpl - addProjectMember] - calling saveProjectUser in repo with org and project IDs");
           projectRepository.saveProjectUser(foundProject.getProjectId(), foundUser.getUserId());
       } catch (DataIntegrityViolationException exception) {
           log.info("[projectFacadeImpl - addProjectMember] - user already exists in project. throwing USER ALREADY MEMBER OF PROJECT");
           throw new UserAlreadyInProjectException(ExceptionReason.USER_ALREADY_MEMBER_OF_PROJECT.getValue());
       }
        log.info("[projectFacadeImpl - addProjectMember] - user added to project. returning project name and user email to controller");
        return new ProjectMemberDTO(projectMemberDTO.getProjectName(), projectMemberDTO.getEmail());
    }

     private ProjectDTO getProjectByOrganisationIdAndName(UUID organisationId, String projectName) {
        log.info("[projectFacadeImpl - getProjectByOrganisationIdAndName] - calling project repo to find project in current organisation");
        Optional<Project> foundProject = projectRepository.findProjectByOrganisationIdAndName(organisationId, projectName);
        if(foundProject.isPresent()) {
            log.info("[projectFacadeImpl - getProjectByOrganisationIdAndName] - project found - calling converter method, toDTO");
            return projectDTOConverter.toDTO(foundProject.get());

        }
        log.info("[projectFacadeImpl - getProjectByOrganisationIdAndName] - project not found - throwing PROJECT NOT FOUND IN ORG exception");
        throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND_IN_ORG.getValue());
    }

    public void deleteProject(UUID id) {
        log.info("[ProjectService- deleteProject] - calling projectRepository to find project by id");
        if (!projectRepository.existsById(id)) {
        log.error("[ProjectService- deleteProject] - Project NOT found in repository");
            throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND.getValue());
        }
        log.info("[ProjectService- deleteProject] - calling projectRepository to delete project by id");
        projectRepository.deleteById(id);
      }

    @Transactional
    public void removeProjectUser(UUID organisationId, UUID projectId, UUID userId) throws OrganisationNotFoundException {
        log.info("[ProjectFacadeImpl - removeProjectUser] - calling organisationFacade to check if org exist by id");
        if(!organisationFacade.existsById(organisationId)) throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
        log.info("[ProjectFacadeImpl - removeProjectUser] - calling existById to check if project exist by id");
        if(!this.existById(projectId)) throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND_IN_ORG.getValue());
        log.info("[ProjectFacadeImpl - removeProjectUser] - calling userFacade to check if user exist by id");
        if(!userFacade.existById(userId)) throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue());
        if(!organisationFacade.userExistInOrganisation(organisationId, userId)) throw new UserNotMemberOfOrganisationException(ExceptionReason.USER_NOT_MEMBER_OF_ORG.getValue());
        if(!projectRepository.userExistInProject(projectId, userId)) throw new UserNotMemberOfProjectException(ExceptionReason.USER_NOT_MEMBER_OF_PROJECT.getValue());

        log.info("[ProjectFacadeImpl - removeProjectUser] - All objects exist. Calling projectRepository to remove user from project");
        projectRepository.removeUserFromProject(projectId, userId);
    }

    @Override
    public boolean existById(UUID projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.isPresent();
    }
 }



