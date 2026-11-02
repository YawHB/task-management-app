package com.todoapp.project.internal.validation;

import com.task_management_app.api.model.ProjectPost;
import com.todoapp.exceptionHandling.exceptions.*;
import com.todoapp.project.internal.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public void ValidateExistsById(UUID projectId) {
        if(!projectRepository.existsById(projectId)){
            throw new ProjectNotFoundException(ExceptionReason.PROJECT_NOT_FOUND.getValue());
        }
    }

    /**
     * Validates that a user is a member of the specified project.
     *
     * @param projectId the ID of the project to check
     * @param userId the ID of the user whose membership is being validated
     * @throws UserNotMemberOfProjectException if the user is not a member of the project
     */
    public void validateUserIsMemberOfProject(UUID projectId, UUID userId){
        if(!projectRepository.userExistInProject(projectId, userId))
            throw new UserNotMemberOfProjectException(ExceptionReason.USER_NOT_MEMBER_OF_PROJECT.getValue());
    }
}
