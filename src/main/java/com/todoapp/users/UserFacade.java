package com.todoapp.users;

import com.task_management_app.api.model.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
     List<User> getAllUsers();

    Optional<User> findUserById(UUID id);


    User addUser(User user);

    UserDTO getUserByEmail(String email);


    void deleteTask(UUID id);

UserDTO findUserByEmail(String userEmail);
}
