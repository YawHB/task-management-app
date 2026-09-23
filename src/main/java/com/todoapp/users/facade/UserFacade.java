package com.todoapp.users.facade;

import com.task_management_app.api.model.UserDTO;
import com.todoapp.users.internal.model.User;

import java.util.List;
import java.util.UUID;

public interface UserFacade {
     List<User> getAllUsers();

   boolean existById(UUID id);

   boolean userExistInOrganisation(UUID userId, UUID organisationId);


    UserDTO addUser(UserDTO userDTO);

    UserDTO getUserByEmail(String email);


    void deleteTask(UUID id);

UserDTO findUserByEmail(String userEmail);
}
