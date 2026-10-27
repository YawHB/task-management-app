package com.todoapp.user.internal.controller;

import com.task_management_app.api.UserApi;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.ProjectNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
import com.todoapp.mediator.MediatorFacade;
import com.todoapp.user.internal.service.UserFacadeImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@RestController
@AllArgsConstructor
@Slf4j
public class UserController implements UserApi {

    private final MediatorFacade mediatorFacadeImpl;


    private final UserFacadeImpl userFacadeImpl;

    @Override
    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {
        log.info("[UserController - addUser] - calling userService to add user");
        UserDTO createdUser = userFacadeImpl.addUser(userDTO);
        log.info("[UserController - addUser] - returning the created user to client");
        return ResponseEntity.status(HttpStatus.CREATED).body( (createdUser));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID id) {
        UserDTO userDTO = userFacadeImpl.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Override
    public ResponseEntity<List<UserSummaryDTO>> getUsersInProject(UUID projectId) {
        log.info("[UserController - getUsersInProject] - calling mediatorFacadeImpl");
        List<UserSummaryDTO> users = mediatorFacadeImpl.getProjectMembers(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public ResponseEntity<List<UserSummaryDTO>> getUsersInOrganisation(UUID organisationId) {
      List<UserSummaryDTO> users = userFacadeImpl.getUsersInOrganisation(organisationId);
      return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}


