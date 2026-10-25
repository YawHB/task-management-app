package com.todoapp.user.facade;

import com.task_management_app.api.model.UserDTO;
import com.todoapp.user.internal.model.User;

import java.util.List;
import java.util.UUID;

public interface UserFacade {

    UserDTO addUser(UserDTO userDTO);

    UserDTO getUserById(UUID userId);

    UserDTO getUserByEmail(String userEmail);

    List<User> getUsersInProject();

    List<User> getUsersInOrganisation();

   boolean existById(UUID id);

   boolean userExistInProject(UUID userId, UUID projectId);

   boolean userExistInOrganisation(UUID userId, UUID organisationId);

    void deleteTask(UUID id);

}
