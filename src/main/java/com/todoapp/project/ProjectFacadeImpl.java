package com.todoapp.project;

import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.organisation.OrganisationFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class ProjectFacadeImpl implements ProjectFacade{

    ProjectDTOConverter projectDTOConverter;
    ProjectRepository projectRepository;
    OrganisationFacade organisationFacade;
    @Override
    public ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) {

        log.info("[ProjectFacadeImpl - addProject] - checks if organisation exists by its id (OBS: missing error handling on fail)");
      if(!organisationFacade.existsById(organisationId)) System.out.println("ADD EXCEPTION HERE"); //TODO - ADD EXCEPTION HERE


      log.info("[ProjectFacadeImpl - addProject] - returns a project from the project DTO converter ");
      Project project = projectDTOConverter.toEntity(organisationId, projectPost);
      log.info("[ProjectFacadeImpl - addProject] - saves Project object ");
      Project createdProject = projectRepository.save(project);
      log.info("[ProjectFacadeImpl - addProject] - returns Project object");
      return projectDTOConverter.toDTO(createdProject);
    }
}


