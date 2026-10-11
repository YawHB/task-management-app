package com.todoapp.exceptionHandling;

import com.task_management_app.api.model.ErrorResponseDTO;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserExistsInOrganisationException;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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

    // 3. User already belongs to organisation -> 409

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

    /**PROJECT EXCEPTIONS **/


    /** TASK EXCEPTIONS **/



}