package com.todoapp.project;


import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptions.OrganisationNotFoundException;

import java.util.UUID;

public interface ProjectFacade {

    ProjectDTO addProject(UUID organisationId, ProjectPost projectPost) throws OrganisationNotFoundException;
   // ProjectDTO getProjectByName(String name);



}


