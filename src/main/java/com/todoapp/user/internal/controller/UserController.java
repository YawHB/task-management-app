package com.todoapp.user.internal.controller;

import com.task_management_app.api.UserApi;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
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

    private final UserFacadeImpl userService;

    @Override
    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {
        log.info("[UserController - addUser] - calling userService to add user");
        UserDTO createdUser = userService.addUser(userDTO);
        log.info("[UserController - addUser] - returning the created user to client");
        return ResponseEntity.status(HttpStatus.CREATED).body( (createdUser));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID id) {
        try {

        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch ( UserNotFoundException exception) {
            log.error("[UserController - getUserById]" + exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<List<OrganisationDTO>> getOrganisationsByUserId(UUID userId) {
       return ResponseEntity.status(HttpStatus.OK).body( userService.getOrganisationsByUserId(userId));
    }
}


