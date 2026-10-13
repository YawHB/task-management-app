package com.todoapp.exceptionHandling;

import com.task_management_app.api.model.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component

public class ErrorResponseFactory {

    // Helper method: build a consistent error response

    public ResponseEntity<ProblemDetail> buildProblem(HttpStatus status, String title, String detail, HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    public ResponseEntity<ErrorResponseDTO> buildCustomProblem(HttpStatus status, String title, String detail, HttpServletRequest request){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setErrorTitle(title);
        errorResponseDTO.setErrorDescription(detail);
        errorResponseDTO.setStatusCode(status.toString());
        errorResponseDTO.setErrorURI(request.getRequestURI());
        return ResponseEntity.status(status).body(errorResponseDTO);
    }
}
