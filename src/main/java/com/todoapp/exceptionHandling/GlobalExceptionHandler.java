package com.todoapp.exceptionHandling;

import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserExistsInOrganisationException;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. Organisation does not exist -> 404

    @ExceptionHandler(OrganisationNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleOrganisationNotFound(OrganisationNotFoundException exception,
                                                                    HttpServletRequest request) {

        log.warn("Organisation not found: {}", exception.getMessage());

        return buildProblem(HttpStatus.NOT_FOUND, "Organisation not found", exception.getMessage(), request);
    }

    // 2. User does not exist -> 404

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException exception, HttpServletRequest request) {

        log.warn("User not found: {}", exception.getMessage());

        return buildProblem(HttpStatus.NOT_FOUND,
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

        return buildProblem(
                HttpStatus.CONFLICT,
                "Membership already exists",
                exception.getMessage(),
                request
        );
    }

    // Helper method: build a consistent error response

    private ResponseEntity<ProblemDetail> buildProblem(HttpStatus status, String title, String detail, HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}