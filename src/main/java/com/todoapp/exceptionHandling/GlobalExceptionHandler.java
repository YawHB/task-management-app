package com.todoapp.exceptionHandling;

import com.task_management_app.api.model.ErrorResponseDTO;
import com.todoapp.exceptionHandling.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorResponseFactory errorResponseFactory;

    /** ORGANISATION EXCEPTIONS **/

    @ExceptionHandler(OrganisationNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrganisationNotFound(OrganisationNotFoundException exception,
                                                                       HttpServletRequest request) {
        log.warn("Organisation not found: {}", exception.getMessage());

        return errorResponseFactory.buildCustomProblem(HttpStatus.NOT_FOUND, "Organisation not found", exception.getMessage(), request);
    }

    /** USER EXCEPTIONS **/


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException exception, HttpServletRequest request) {

        log.warn("User not found: {}", exception.getMessage());

        return errorResponseFactory.buildProblem(HttpStatus.NOT_FOUND,
                "User not found",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserNotMemberOfOrganisationException.class)
        public ResponseEntity<ErrorResponseDTO> handleUserNotMemberOfOrganisation(UserNotMemberOfOrganisationException  execption, HttpServletRequest request) {
        log.warn("User not member of organisation: {}", execption.getMessage());

        return errorResponseFactory.buildCustomProblem(
                HttpStatus.FORBIDDEN,
                "User denied this organisation",
                execption.getMessage(),
                request);
    }

    @ExceptionHandler(UserExistsInOrganisationException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExists(
            UserExistsInOrganisationException exception,
            HttpServletRequest request) {

        log.warn("Membership conflict: {}", exception.getMessage());

        return errorResponseFactory.buildProblem(
                HttpStatus.CONFLICT,
                "Membership already exists",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserAlreadyInProjectException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistInProject(
            UserAlreadyInProjectException exception,
            HttpServletRequest request){

        return errorResponseFactory.buildCustomProblem(
                HttpStatus.CONFLICT,
                "User already exist",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserNotMemberOfProjectException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserUserNotMemberOfProject(
            UserNotMemberOfProjectException exception,
            HttpServletRequest request){

        return errorResponseFactory.buildCustomProblem(
                HttpStatus.NOT_FOUND,
                "User does not exist in project",
                exception.getMessage(),
                request
        );
    }

    /**PROJECT EXCEPTIONS **/

    @ExceptionHandler(DuplicateProjectNameException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateProjectName(DuplicateProjectNameException exception,
                                                                       HttpServletRequest request) {
        log.warn("Duplicate project name: {}", exception.getMessage());

        return errorResponseFactory.buildCustomProblem(HttpStatus.BAD_REQUEST, "Duplicate project name", exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidRequestBody(InvalidRequestBodyException exception,
                                                                       HttpServletRequest request) {
        log.warn("Empty project request body: {}", exception.getMessage());

        return errorResponseFactory.buildCustomProblem(HttpStatus.BAD_REQUEST, "Empty project request body", exception.getMessage(), request);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProjectNotFound(ProjectNotFoundException exception,
                                                                     HttpServletRequest request) {
        log.warn("Project not found: {}", exception.getMessage());

        return errorResponseFactory.buildCustomProblem(HttpStatus.NOT_FOUND, "Project not found", exception.getMessage(), request);
    }

    /** TASK EXCEPTIONS **/


    @ExceptionHandler(ParentTaskNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleParentTaskNotFound(ParentTaskNotFoundException exception, HttpServletRequest request) {
        log.warn("Project not found: {}", exception.getMessage());
        return errorResponseFactory.buildCustomProblem(HttpStatus.NOT_FOUND, "Parent task not found", exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorResponseDTO> handlePInvalidDateRange(InvalidDateRangeException exception,
                                                                     HttpServletRequest request) {
        log.warn("Invalid date range: {}", exception.getMessage());

        return errorResponseFactory.buildCustomProblem(HttpStatus.NOT_FOUND, "Invalid date range", exception.getMessage(), request);
    }

    @ExceptionHandler(TaskTitleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleTaskTitleAlreadyExist(TaskTitleAlreadyExistsException exception, HttpServletRequest request) {
        log.warn("Title already exist: {}", exception.getMessage());
        return errorResponseFactory.buildCustomProblem(HttpStatus.NOT_FOUND, "Invalid task title. Title already exist", exception.getMessage(), request);
    }

    @ExceptionHandler(TaskNotFoundInProjectException.class)
    public ResponseEntity<ErrorResponseDTO> handleTaskNotFoundInProject(TaskNotFoundInProjectException exception, HttpServletRequest request) {
        log.warn("Task not found in project: {}", exception.getMessage());
        return errorResponseFactory.buildCustomProblem(HttpStatus.NOT_FOUND, "Task not found in project", exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {

        log.warn("Invalid user input: {}", exception.getMessage());
        return errorResponseFactory.buildCustomProblem(HttpStatus.BAD_REQUEST, "Invalid user input", exception.getMessage(), request);

        }
    }