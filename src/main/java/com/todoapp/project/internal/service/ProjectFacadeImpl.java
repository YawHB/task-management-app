package com.todoapp.project.internal.service;

import com.task_management_app.api.model.*;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.project.facade.ProjectProjection;
import com.todoapp.project.facade.mapper.ProjectMapper;
import com.todoapp.project.internal.repository.ProjectRepository;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.project.facade.ProjectSummaryProjection;
import com.todoapp.project.internal.model.Project;
import com.todoapp.project.internal.validation.ProjectValidationHandler;
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

    private final ProjectValidationHandler validationHandler;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) {

          validationHandler.validateAddProject(projectPost);
          log.info("[ProjectFacadeImpl - addProject] - calls projectMapper toEntity");
          Project project = projectMapper.toEntity(organisationId, projectPost);
          log.info("[ProjectFacadeImpl - addProject] - saves Project object");
           Project createdProject = projectRepository.save(project);
          log.info("[ProjectFacadeImpl - addProject] - fetches new project");
           ProjectProjection projectProjection = projectRepository.findProjectWithMembersById(createdProject.getId());

          log.info("[ProjectFacadeImpl - addProject] - returns Project object");
          return projectMapper.toDTO(projectProjection);
    }

    @Override
    public List<ProjectSummaryDTO> getProjectsForUserInOrganisation(UUID organisationId, UUID userId) throws OrganisationNotFoundException {

        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling projectRepository with org id and user id to find all projects in org for user");
        List<ProjectSummaryProjection> projects = projectRepository.findProjectsForMemberInOrganisation(organisationId, userId);
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] - calling toDTO");
        List<ProjectSummaryDTO> projectSummaryDTOS = projectMapper.toProjectSummaryDTO(projects);
        log.info("[ProjectFacadeImpl - getProjectsForMemberInOrganisation] -return a list of projectSummaryDTOs to controller");
        return projectSummaryDTOS;
    }

    @Transactional
    public ProjectMemberDTO addProjectUser(UUID organisationId, ProjectMemberDTO projectMemberDTO, UUID userId) {
        log.info("[projectFacadeImpl - addProjectUser] - calling userFacade to get user by email");
       try{
            log.info("[projectFacadeImpl - addProjectUser] - calling private method getProjectByOrganisationIdAndName");
            ProjectDTO foundProject = getProjectByOrganisationIdAndName( organisationId, projectMemberDTO.getProjectName());
            log.info("[projectFacadeImpl - addProjectUser] - calling saveProjectUser in repo with org and project IDs");
            projectRepository.saveProjectUser(foundProject.getProjectId(), userId);
            log.info("[projectFacadeImpl - addProjectUser] - user added to project. returning project name and user email to controller");
        return new ProjectMemberDTO(projectMemberDTO.getProjectName(), projectMemberDTO.getEmail());

       } catch (DataIntegrityViolationException exception) {
           throw new UserAlreadyInProjectException(ExceptionReason.USER_ALREADY_MEMBER_OF_PROJECT.getValue());
       }
    }

     private ProjectDTO getProjectByOrganisationIdAndName(UUID organisationId, String projectName) {
        log.info("[projectFacadeImpl - getProjectByOrganisationIdAndName] - calling project repo to find project in current organisation");
        Optional<Project> foundProject = projectRepository.findProjectByOrganisationIdAndName(organisationId, projectName);
        if(foundProject.isPresent()) {
            log.info("[projectFacadeImpl - getProjectByOrganisationIdAndName] - project found - calling projectMapper -> toDTO");
            return projectMapper.toDTO(foundProject.get());
        }
        log.info("[projectFacadeImpl - getProjectByOrganisationIdAndName] - project not found - throwing PROJECT NOT FOUND IN ORG exception");
        throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND_IN_ORG.getValue());
    }

    //TODO - delegate to validator class
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
    public void removeProjectUser(UUID projectId, UUID userId){
        log.info("[ProjectFacadeImpl - removeProjectUser] - calls validator if user is member of project");
        validationHandler.validateUserIsMemberOfProject(projectId,userId);
        log.info("[ProjectFacadeImpl - removeProjectUser] - All objects exist. Calling projectRepository to remove user from project");
        projectRepository.removeUserFromProject(projectId, userId);
    }

    @Override
    public void existsById(UUID projectId) {
        log.info("[ProjectFacadeImpl - existsById] - validate user exists by id");
        validationHandler.ValidateExistsById(projectId);
    }
 }



