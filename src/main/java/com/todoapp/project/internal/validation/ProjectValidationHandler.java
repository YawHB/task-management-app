package com.todoapp.project.internal.validation;

import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptionHandling.exceptions.DuplicateProjectNameException;
import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.InvalidRequestBodyException;
import com.todoapp.project.internal.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ProjectValidationHandler {

    private final ProjectRepository projectRepository;

    public void validateAddProject(ProjectPost newProject) {
        String name = newProject.getName();

        validateNotBlank(name);
        validateProjectNameIsUnique(name);


    }

    public void validateNotBlank(String value) {
        log.info("[ProjectValidationHandler - validateNotBlank] - validates project name in request body is valid)");
        if(value == null || value.isBlank() ) throw new InvalidRequestBodyException(ExceptionReason.INVALID_REQUEST_BODY.getValue());
    }

    public void validateProjectNameIsUnique(String name) {
        log.info("[ProjectValidationHandler - validateNotBlank] - validate uniqueness of project name)");
        if(projectRepository.existsByName(name)) throw new DuplicateProjectNameException(ExceptionReason.INVALID_REQUEST_BODY.getValue());
    }

}
