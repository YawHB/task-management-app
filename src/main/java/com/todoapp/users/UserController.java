package com.todoapp.users;

import com.task_management_app.api.UserApi;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptions.UserNotFoundException;
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

    private final UserFacadeImpl userFacadeService;
    private final UserFacadeImpl userService;
    private final UserDTOConverter userDTOConverter;

    @Override
    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {

        User createdUser = userFacadeService.addUser(userDTOConverter.toEntity(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body( userDTOConverter.toDTO(createdUser));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<List<OrganisationDTO>> getOrganisationsByUserId(UUID userId) {
        try{
       return ResponseEntity.status(HttpStatus.OK).body( userService.getOrganisationsByUserId(userId));
        } catch (UserNotFoundException exception) {
            log.error("[UserController - getOrganisationsByUserId] - User Not found");
            return ResponseEntity.notFound().build();
        }
    }
}


