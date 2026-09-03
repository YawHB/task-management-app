package com.todoapp.project;


import com.task_management_app.api.model.ProjectDTO;
import com.task_management_app.api.model.ProjectPost;

import java.util.UUID;

public interface ProjectFacade {

    ProjectDTO addProject(UUID organisationId, ProjectPost projectPost);



}


